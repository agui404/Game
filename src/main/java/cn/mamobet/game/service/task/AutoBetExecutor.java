package cn.mamobet.game.service.task;

import cn.mamobet.game.common.CommonEnum;
import cn.mamobet.game.exception.BusinessException;
import cn.mamobet.game.model.dto.AutoBetDTO;
import cn.mamobet.game.model.vo.BetOrderVO;
import cn.mamobet.game.service.BetOrderService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 自动投注执行器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AutoBetExecutor {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final BetOrderService betOrderService;
    private final AutoBetResultHolder resultHolder;

    /**
     * 启动自动投注
     */
    public Future<?> start(AutoBetDTO autoBetDTO, Runnable onFinish) {
        Long userId = autoBetDTO.getUserId();
        int betCount = autoBetDTO.getBetCount();

        // 创建 Session
        resultHolder.createSession(userId, betCount);

        return executor.submit(() -> {
            log.info("【AutoBet】开始自动投注 userId={} count={}", userId, betCount);
            try {
                int count = 0;
                BigDecimal currentBetAmount = autoBetDTO.getBetAmount();
                BigDecimal stopProfit = autoBetDTO.getStopProfit();
                BigDecimal stopLoss = autoBetDTO.getStopLoss();
                BigDecimal winInc = autoBetDTO.getWinIncrement();
                BigDecimal lossInc = autoBetDTO.getLossIncrement();

                BigDecimal totalBet = BigDecimal.ZERO;
                BigDecimal totalPayout = BigDecimal.ZERO;

                while (betCount == 0 || count < betCount) {
                    // 单局下单
                    BetOrderVO order;
                    try {
                        order = betOrderService.placeOrder(autoBetDTO.toBetOrderDTO(currentBetAmount));
                        resultHolder.saveOrder(userId, order);
                    } catch (BusinessException e) {
                        log.error("【AutoBet】业务异常", e);
                        resultHolder.setBusinessInfo(userId, e.getMessage());
                        break;
                    }
                    totalBet = totalBet.add(order.getBetAmount());
                    totalPayout = totalPayout.add(order.getPayoutAmount());
                    BigDecimal netLoss = totalBet.subtract(totalPayout);

                    // 判断止盈止损
                    if (stopProfit != null && stopProfit.compareTo(BigDecimal.ZERO) > 0 && netLoss.compareTo(BigDecimal.ZERO) < 0 && netLoss.abs().compareTo(stopProfit) >= 0) {
                        log.info("【AutoBet】达到止盈，停止");
                        break;
                    }
                    if (stopLoss != null && stopLoss.compareTo(BigDecimal.ZERO) > 0 && netLoss.compareTo(BigDecimal.ZERO) > 0 && netLoss.compareTo(stopLoss) >= 0) {
                        log.info("【AutoBet】达到止损，停止");
                        break;
                    }

                    // 更新下局投注额
                    CommonEnum.YesOrNo isWin = order.getIsWin();
                    if (CommonEnum.YesOrNo.YES == isWin) {
                        currentBetAmount = winInc != null && winInc.compareTo(BigDecimal.ZERO) > 0 ? currentBetAmount.multiply(BigDecimal.ONE.add(winInc)) : currentBetAmount;
                    } else {
                        currentBetAmount = lossInc != null && lossInc.compareTo(BigDecimal.ZERO) > 0 ? currentBetAmount.multiply(BigDecimal.ONE.add(lossInc)) : currentBetAmount;
                    }

                    count++;
                    Thread.sleep(1000); // 可调节，避免过快
                }
                log.info("【AutoBet】自动投注结束 userId={} 完成 {} 局", userId, count);
            } catch (Exception e) {
                log.error("【AutoBet】执行异常", e);
            } finally {
//                resultHolder.removeSession(userId);
                //回调任务=》自动清理 taskMap
//                if (onFinish != null) {
//                    onFinish.run();
//                }
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdownNow();
    }
}
