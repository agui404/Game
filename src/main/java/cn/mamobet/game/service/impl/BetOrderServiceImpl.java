package cn.mamobet.game.service.impl;

import cn.mamobet.game.common.BusinessCode;
import cn.mamobet.game.common.CommonEnum;
import cn.mamobet.game.entity.BetOrder;
import cn.mamobet.game.entity.BetUser;
import cn.mamobet.game.exception.BusinessException;
import cn.mamobet.game.mapper.BetOrderMapper;
import cn.mamobet.game.model.BetCalcParam;
import cn.mamobet.game.model.dto.BetCalcResultDTO;
import cn.mamobet.game.model.dto.BetOrderDTO;
import cn.mamobet.game.model.vo.BetOrderVO;
import cn.mamobet.game.service.BetOrderService;
import cn.mamobet.game.service.BetUserService;
import cn.mamobet.game.util.BetCalculatorUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetOrderServiceImpl extends ServiceImpl<BetOrderMapper, BetOrder> implements BetOrderService {

    private final BetUserService betUserService;

    /**
     * 创建投注单并开奖
     */
    @Override
    @Transactional
    public BetOrderVO placeOrder(BetOrderDTO betOrderDTO) {
        //校验用户是否存在
        BetUser user = betUserService.getById(betOrderDTO.getUserId());
        if (user == null) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND);
        }

        //校验投注金额
        BigDecimal betAmount = betOrderDTO.getBetAmount();
        if (betAmount == null || betAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(BusinessCode.BET_AMOUNT_TOO_LOW);
        }
        //校验投注金额超过最低
        if (betAmount.compareTo(new BigDecimal("0.0001")) < 0) {
            throw new BusinessException(BusinessCode.BET_AMOUNT_TOO_LOW);
        }
        //校验投注金额和账户余额是否可用
        if (betAmount.compareTo(user.getBalance()) > 0) {
            throw new BusinessException(BusinessCode.BET_AMOUNT_TOO_HIGH);
        }

        //校验派彩值
        BigDecimal payoutRatio = betOrderDTO.getPayoutRatio();
        if (payoutRatio == null || payoutRatio.compareTo(new BigDecimal("1.0102")) < 0
                || payoutRatio.compareTo(new BigDecimal("99.0000")) > 0) {
            throw new BusinessException(BusinessCode.INVALID_PAYOUT);
        }

        //派彩计算器 ,暂且只通过根据派彩值计算
        BetCalcParam betCalcParam = new BetCalcParam();
        betCalcParam.setCalcMode(CommonEnum.CalcMode.BY_PAYOUT);
        betCalcParam.setValue(payoutRatio);
        BetCalcResultDTO calcResultDTO = BetCalculatorUtil.calculate(betCalcParam);

        BigDecimal rollUnder = calcResultDTO.getRollUnder(); // 掷小于阈值
        BigDecimal rollOver = calcResultDTO.getRollOver();   // 掷大于阈值

        //获取前端传递的投注条件（大于 / 小于）
        BetOrderDTO.BetCondition betCondition = betOrderDTO.getBetCondition();
        if (betCondition == null) {
            // 如果没传，默认大于
            betCondition = BetOrderDTO.BetCondition.OVER;
        }

        //扣除余额（先扣款） todo 暂且不加锁
        user.setBalance(user.getBalance().subtract(betAmount));
        betUserService.updateById(user);

        //开奖逻辑：生成 [0.00, 100.00) 的随机数，保留 2 位小数
        BigDecimal randomNumber = new BigDecimal(String.format("%.2f", Math.random() * 100));
        boolean isWin;

        if (betCondition == BetOrderDTO.BetCondition.OVER) {
            // 掷大于：押大于 rollOver
            isWin = randomNumber.compareTo(rollOver) > 0;  // > 阈值才赢
        } else {
            // 掷小于：押小于 rollUnder
            isWin = randomNumber.compareTo(rollUnder) < 0; // < 阈值才赢
        }
        CommonEnum.YesOrNo orderWinStatus = isWin ? CommonEnum.YesOrNo.YES : CommonEnum.YesOrNo.NO;


        //若未中奖则返奖=0，若中奖则返奖 = 投注额 *派彩值
        BigDecimal payoutAmount = isWin ? betAmount.multiply(payoutRatio).setScale(4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        //中奖就加钱  todo 暂且不加锁
        if (isWin) {
            user.setBalance(user.getBalance().add(payoutAmount));
            betUserService.updateById(user);
        }

        //保存投注单
        BetOrder betOrder = new BetOrder();
        betOrder.setUserId(user.getId());
        betOrder.setBetAmount(betAmount);
        betOrder.setPayoutRatio(payoutRatio);
        betOrder.setBetType(betOrderDTO.getBetType());
        betOrder.setResultNumber(randomNumber);
        betOrder.setIsWin(orderWinStatus);
        betOrder.setPayoutAmount(payoutAmount);
        betOrder.setStatus(BetOrder.Status.COMPLETED);

        save(betOrder);
        log.info("calcResultDTO:{},betOrder:{}", calcResultDTO, betOrder);

        //返回前端
        return BetOrderVO.builder().orderId(betOrder.getId()).userId(betOrder.getUserId()).betAmount(betOrder.getBetAmount())
                .payoutRatio(betOrder.getPayoutRatio()).betType(betOrder.getBetType()).resultNumber(betOrder.getResultNumber())
                .isWin(orderWinStatus).payoutAmount(payoutAmount).status(betOrder.getStatus()).build();
    }


    @Override
    public BetOrder getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<BetOrder> getOrdersByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
}
