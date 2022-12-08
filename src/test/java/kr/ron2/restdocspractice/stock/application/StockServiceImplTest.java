package kr.ron2.restdocspractice.stock.application;

import groovy.util.logging.Slf4j;
import kr.ron2.restdocspractice.stock.domain.Stock;
import kr.ron2.restdocspractice.stock.infra.StockJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;

import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StockServiceImplTest {

    @Autowired
    private StockService stockService;
    @Autowired
    private StockJpaRepository stockJpaRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @DisplayName("재고감소")
    @Test
    void orderTest() {
        stockService.order(1L, 10L);
        Stock stock = stockJpaRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        Assertions.assertThat(stock.getQuantity()).isEqualTo(90L);
    }

    @Test
    void decreaseTest() {
        stockService.decrease(1L, 10L);

    }

    @Test
    void setKeyTest() {
        redisService.setKey("");
    }

    @Test
    void oneCommandSetKeyTest() {
        redisService.oneCommandSet("no-transaction");
    }

    @Test
    void setnxTest() throws InterruptedException {

        boolean strkey = redisService.setLockAndIncr("strkey", 10);
        System.out.println(strkey);
    }

    @Test
    void setnxTestMultiThread() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(10);
        int count = 10;
        while (count > 0) {
            executorService.execute(() -> {
                try {
                    redisService.setLockAndIncr("strkey", 10);

                } catch (InterruptedException | RuntimeException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            count--;
            Thread.sleep(100);
        }
        countDownLatch.await();
        String strkey = (String) redisTemplate.opsForValue().get("strkey");
        System.out.println(strkey);
    }
}
