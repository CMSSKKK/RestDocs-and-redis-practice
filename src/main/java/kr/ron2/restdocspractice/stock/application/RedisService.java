package kr.ron2.restdocspractice.stock.application;

import org.springframework.data.redis.core.SessionCallback;

public interface RedisService {

    boolean setKey(String key);

    void oneCommandSet(String key);

    boolean setLockAndIncr(String key, long count) throws InterruptedException;

    boolean getCoupon(String couponKey);

}
