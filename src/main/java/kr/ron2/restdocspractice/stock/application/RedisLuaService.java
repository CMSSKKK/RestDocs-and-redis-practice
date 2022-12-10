package kr.ron2.restdocspractice.stock.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class RedisLuaService implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<Boolean> couponLimiter;

    @Override
    public boolean setKey(String key) {
        return false;
    }

    @Override
    public void oneCommandSet(String key) {

    }

    @Override
    public boolean setLockAndIncr(String key, long count) throws InterruptedException {
        return false;
    }

    @Override
    public boolean getCoupon(String couponKey) {
        Boolean success = redisTemplate.execute(couponLimiter, List.of(couponKey));
        log.info("thread = {}, success = {}", Thread.currentThread(), success);
        return success;
    }
}
