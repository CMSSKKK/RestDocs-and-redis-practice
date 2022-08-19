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
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;

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

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @BeforeEach
    void setUp() {
        Stock item = Stock.of("아이템", 100L);

        Stock saved = stockJpaRepository.save(item);
        String key = String.format("stock:%d", saved.getId());
        redisTemplate.watch(key);
        Object execute = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                //redisTemplate.watch(key);
                redisTemplate.multi();

                SetOperations setOperations = operations.opsForSet();
                setOperations.add(String.format("stock:%d", saved.getId()), saved.getQuantity() + "");
                return operations.exec();
            }
        });

        System.out.println(execute);
    }

    @AfterEach
    void tearDown() {
        stockJpaRepository.deleteAll();
    }

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

    @DisplayName("재고감소 동시성 테스트")
    @Test
    void order() throws InterruptedException {
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                        stockService.decrease(1L, 10L);
                        countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        //Stock stock = stockJpaRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        //Assertions.assertThat(stock.getQuantity()).isEqualTo(0);
    }
}
