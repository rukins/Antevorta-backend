package com.antevorta.repository.redis;

import com.antevorta.security.encryptor.Encryptor;
import com.antevorta.service.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class AccessTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    private final CurrentUserService currentUserService;

    private final Encryptor encryptor;

    @Autowired
    public AccessTokenRepository(RedisTemplate<String, String> redisTemplate,
                                 CurrentUserService currentUserService, Encryptor encryptor) {
        this.redisTemplate = redisTemplate;
        this.currentUserService = currentUserService;
        this.encryptor = encryptor;
    }

    public void save(String keyPrefix, String accessToken, Long timeToLive) {
        accessToken = encryptor.encrypt(accessToken);

        redisTemplate.opsForValue().set(
                keyPrefix + currentUserService.getAuthorizedUser().getEmail(),
                accessToken,
                Duration.ofSeconds(timeToLive)
        );
    }

    public String getByKeyPrefix(String keyPrefix) {
        String encryptedAccessToken = redisTemplate.opsForValue().get(
                keyPrefix + currentUserService.getAuthorizedUser().getEmail()
        );

        return encryptor.decrypt(encryptedAccessToken);
    }
}
