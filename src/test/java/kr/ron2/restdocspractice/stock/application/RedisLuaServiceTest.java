package kr.ron2.restdocspractice.stock.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisLuaServiceTest {

    @Autowired
    private RedisLuaService redisLuaService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ExecutorService executorService = Executors.newFixedThreadPool(15);

    @Test
    void getCoupon() throws InterruptedException {
        String couponKey = "coupon:20";
        redisTemplate.opsForValue().set(couponKey, String.valueOf(10));
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                try {
                    redisLuaService.getCoupon(couponKey);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }

            });
        }
        countDownLatch.await();

        String value = redisTemplate.opsForValue().get(couponKey);
        assertThat(Integer.parseInt(value)).isZero();

    }
}
