package kr.ron2.restdocspractice.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Configuration
public class EmbeddedRedisConfig {

    private final RedisProperties redisProperties;
    private RedisServer redisServer;

    public EmbeddedRedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @PostConstruct
    public void redisServer() {
        redisServer = new RedisServer(redisProperties.getPort());
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if(redisServer != null) {
            redisServer.stop();
        }
    }


}
