package cn.mamobet.game.aop;

import cn.mamobet.game.common.BusinessCode;
import cn.mamobet.game.exception.BusinessException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 限流
 */
@Aspect
@Component
public class RateLimitAspect {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * 后期可改成redis的限流方式
     */
    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        String key = pjp.getSignature().toShortString();
        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket(rateLimit));
        if (bucket.tryConsume(1)) {
            return pjp.proceed();
        } else {
            throw new BusinessException(BusinessCode.TOO_MANY_REQUESTS);
        }
    }

    private Bucket createBucket(RateLimit rateLimit) {
        Refill refill = Refill.greedy(rateLimit.limit(), Duration.ofSeconds(rateLimit.time()));
        Bandwidth limit = Bandwidth.classic(rateLimit.limit(), refill);
        return Bucket4j.builder().addLimit(limit).build();
    }
}