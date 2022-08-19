package kr.ron2.restdocspractice.stock.application;

import kr.ron2.restdocspractice.stock.domain.Stock;
import kr.ron2.restdocspractice.stock.infra.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockJpaRepository stockJpaRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    @Transactional
    public Long order(Long stockId, Long quantity) {
        Stock stock = stockJpaRepository.findById(stockId)
                .orElseThrow(NoSuchElementException::new);

        stock.decrease(quantity);

        return stock.getQuantity();
    }

    @Override
    //@Transactional
    public void decrease(Long stockId, Long quantity) {
        SetOperations<String, Object> stringObjectSetOperations = redisTemplate.opsForSet();
        String key = String.format("stock:%d", stockId);
        redisTemplate.watch(key);
        String pop = (String) stringObjectSetOperations.pop(key);

        if(pop == null) {
            pop = "0";
        }
        System.out.println("POP========="+pop);
        long quauntity = Long.parseLong(pop) - quantity;
        //System.out.println(quauntity);
        Object execute = redisTemplate.execute(new SessionCallback() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //operations.watch(key);
                operations.multi();

                SetOperations setOperations = operations.opsForSet();
                setOperations.add(key, quantity+"");
                return operations.exec();
            }
        });

    }
}
