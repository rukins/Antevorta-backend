package com.antevorta.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class VerificationCodeRepository {
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public VerificationCodeRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String keyPrefix, Integer code, Long timeToLive, String email) {
        redisTemplate.opsForValue().set(
                getKeyPrefix(keyPrefix, email),
                String.valueOf(code),
                Duration.ofSeconds(timeToLive)
        );
    }

    public Integer getByKeyPrefix(String keyPrefix, String email) {
        String code = redisTemplate.opsForValue().get(
                getKeyPrefix(keyPrefix, email)
        );

        return code != null ? Integer.parseInt(code) : null;
    }

    private String getKeyPrefix(String keyPrefix, String email) {
        return keyPrefix + email;
    }
}
