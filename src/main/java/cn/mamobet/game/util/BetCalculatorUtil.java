package cn.mamobet.game.util;

import cn.mamobet.game.common.BusinessCode;
import cn.mamobet.game.exception.BusinessException;
import cn.mamobet.game.model.BetCalcParam;
import cn.mamobet.game.model.dto.BetCalcResultDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 派彩 & 几率 & 掷数 计算结果
 */
public class BetCalculatorUtil {

    /**
     * 根据派彩值或获胜几率，双向计算其余值
     *
     * @param param 入参（派彩值或获胜几率，必须传一个，另一个传 null）
     * @return 计算结果
     */
    public static BetCalcResultDTO calculate(BetCalcParam param) {
        final BigDecimal FEE = new BigDecimal("0.01");
        BigDecimal payoutRatio;//派彩值
        BigDecimal winRate;//获胜几率

        switch (param.getCalcMode()) {
            // 已知派彩 -> 算获胜几率 ，调整派彩时，获胜几率(%)  = ROUND（（1 -1 %）/ 派彩金额 * 100  ,  2)
            case BY_PAYOUT -> {
                payoutRatio = param.getValue();
                winRate = BigDecimal.ONE
                        .subtract(FEE)
                        .divide(payoutRatio, 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }
            // 已知获胜几率 -> 算派彩 ，调整获胜几率(%) 时，派彩金额  = ROUND（（1 -1 %）/ 获胜几率* 100  ,  4)
            case BY_WIN_RATE -> {
                winRate = param.getValue();
                payoutRatio = BigDecimal.ONE
                        .subtract(FEE)
                        .divide(winRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP), 10, RoundingMode.HALF_UP)
                        .setScale(4, RoundingMode.HALF_UP);
            }
            default -> throw new BusinessException(BusinessCode.UNKNOWN_ERROR);
        }

        // 检查合法范围,即命中概率，最小为0.01，低于则报错【见State-3】。最大为98.00，高于则报错【见State-4】。其调整也会影响到派彩。小数点后2位。
        if (winRate.compareTo(new BigDecimal("0.01")) < 0 || winRate.compareTo(new BigDecimal("98.00")) > 0) {
            throw new BusinessException(BusinessCode.INVALID_PROBABILITY);
        }
        //校验派彩值
        checkPayoutRatio(payoutRatio);

        // 掷小于
        BigDecimal rollUnder = winRate;
        //掷大于 = 100.00 - 掷小于
        BigDecimal rollOver = BigDecimal.valueOf(100).subtract(rollUnder);
        return BetCalcResultDTO.builder().payoutRatio(payoutRatio).winRate(winRate).rollUnder(rollUnder).rollOver(rollOver).build();
    }


    /**
     * 校验派彩值
     */
    public static void checkPayoutRatio(BigDecimal payoutRatio) {
        if (payoutRatio == null || payoutRatio.compareTo(new BigDecimal("1.0102")) < 0
                || payoutRatio.compareTo(new BigDecimal("99.0000")) > 0) {
            throw new BusinessException(BusinessCode.INVALID_PAYOUT);
        }
    }
}
