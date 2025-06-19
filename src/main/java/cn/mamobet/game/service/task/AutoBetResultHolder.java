package cn.mamobet.game.service.task;

import cn.mamobet.game.model.vo.BetOrderVO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 保存自动投注的进度与历史
 */
@Component
public class AutoBetResultHolder {

    private final Cache<Long, AutoBetSession> sessionCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES) // 每条最多活 10 分钟
            .build();
    public void createSession(Long userId, int targetCount) {
        AutoBetSession session = new AutoBetSession();
        session.setTargetCount(targetCount);
        sessionCache.put(userId, session);
    }

    public void saveOrder(Long userId, BetOrderVO order) {
        AutoBetSession session = sessionCache.getIfPresent(userId);
        if (session != null) {
            session.getHistory().add(order);
            session.setFinishedCount(session.getFinishedCount() + 1);
            session.setTotalBetAmount(session.getTotalBetAmount().add(order.getBetAmount()));
            session.setTotalPayout(session.getTotalPayout().add(order.getPayoutAmount()));
            session.setNetLoss(session.getTotalBetAmount().subtract(session.getTotalPayout()));
        }
    }

    public AutoBetSession getSession(Long userId) {
        return sessionCache.getIfPresent(userId);
    }

    public void removeSession(Long userId) {
        sessionCache.invalidate(userId);
    }

    //错误业务日志
    public void setBusinessInfo(Long userId, String businessInfo) {
        AutoBetSession session = sessionCache.getIfPresent(userId);
        if (session != null) {
            session.setMsg(businessInfo);
        }
    }

    @Data
    public static class AutoBetSession {
        private int finishedCount = 0;
        private int targetCount;
        private BigDecimal totalBetAmount = BigDecimal.ZERO;
        private BigDecimal totalPayout = BigDecimal.ZERO;
        private BigDecimal netLoss = BigDecimal.ZERO;
        private List<BetOrderVO> history = new ArrayList<>();
        private String msg = new String();
    }
}
