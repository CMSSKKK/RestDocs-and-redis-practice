package kr.ron2.restdocspractice.stock.application;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean setKey(String key) {

        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
                stringValueOperations.get("stock:1");
                stringValueOperations.set("stock:2", String.valueOf(10));

                List<Object> exec = redisTemplate.exec();
                exec.forEach(System.out::println);
                return exec;

            }});

        return true;

    }

    @Override
    public void oneCommandSet(String key) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, key);
    }

    @Override
    public boolean setLockAndIncr(String key, long count) throws InterruptedException {

        Boolean isPossible = redisTemplate.opsForValue().setIfAbsent("mylock", "1");

        if (Boolean.FALSE.equals(isPossible)) {
            throw new RuntimeException();
        }

        List<Object> execute = redisTemplate.execute(new SessionCallback<>() {
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().increment((K) key, count);
                operations.opsForValue().getAndDelete((K) "mylock");
                return operations.exec();
            }
        });
        String o = (String) execute.get(1);
        System.out.println(o);
        return o.equals("1");

    }
}
