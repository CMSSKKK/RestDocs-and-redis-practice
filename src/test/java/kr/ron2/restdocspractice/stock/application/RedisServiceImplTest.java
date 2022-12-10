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
class RedisServiceImplTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Test
    void getCoupon() {
        String couponKey = "coupon:10"; // 10% 쿠폰이라 가정
        redisTemplate.opsForValue().set(couponKey, String.valueOf(100));

        boolean coupon = redisService.getCoupon(couponKey);
        assertThat(coupon).isTrue();
        String s = redisTemplate.opsForValue().get(couponKey);
        assertThat(Integer.parseInt(s)).isEqualTo(99);
    }

    @Test
    void getCoupon_fail() {
        String couponKey = "coupon:10"; // 10% 쿠폰이라 가정
        redisTemplate.opsForValue().set(couponKey, String.valueOf(0));

        assertThatThrownBy(() -> redisService.getCoupon(couponKey))
                .isInstanceOf(RuntimeException.class);
        String s = redisTemplate.opsForValue().get(couponKey);
        assertThat(Integer.parseInt(s)).isLessThan(0);

    }

    @Test
    void getCouponMultiThread() throws InterruptedException {
        String couponKey = "coupon:10";
        redisTemplate.opsForValue().set(couponKey, String.valueOf(10));

        CountDownLatch countDownLatch = new CountDownLatch(10);
        int count = 10;
        while (count > 0) {
            executorService.execute(() -> {
                try {
                    redisService.getCoupon(couponKey);

                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            count--;
            Thread.sleep(100);
        }
        countDownLatch.await();

        String s = redisTemplate.opsForValue().get(couponKey);
        assertThat(Integer.parseInt(s)).isEqualTo(0);
    }
}
