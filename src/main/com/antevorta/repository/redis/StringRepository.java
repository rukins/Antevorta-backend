package com.antevorta.repository.redis;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
public class StringRepository {
    private final Jedis redisClient;

    public StringRepository(Jedis redisClient) {
        this.redisClient = redisClient;
    }

    public void save(String key, String value) {
        redisClient.set(key, value);
    }

    public void save(String key, String value, Long timeToLive) {
        redisClient.set(key, value);
        redisClient.expire(key, timeToLive);
    }

    public String get(String key) {
        return redisClient.get(key);
    }
}
