package com.antevorta.repository.redis;

import com.antevorta.security.encryptor.Encryptor;
import com.antevorta.service.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class AccessTokenRepository {
    private final StringRedisTemplate redisTemplate;

    private final CurrentUserService currentUserService;

    private final Encryptor encryptor;

    @Autowired
    public AccessTokenRepository(StringRedisTemplate redisTemplate,
                                 CurrentUserService currentUserService, Encryptor encryptor) {
        this.redisTemplate = redisTemplate;
        this.currentUserService = currentUserService;
        this.encryptor = encryptor;
    }

    public void save(String keyPrefix, String accessToken, Long timeToLive, String arbitraryStoreName) {
        accessToken = encryptor.encrypt(accessToken);

        redisTemplate.opsForValue().set(
                getKeyPrefix(keyPrefix, arbitraryStoreName),
                accessToken,
                Duration.ofSeconds(timeToLive)
        );
    }

    public String getByKeyPrefix(String keyPrefix, String arbitraryStoreName) {
        String encryptedAccessToken = redisTemplate.opsForValue().get(
                getKeyPrefix(keyPrefix, arbitraryStoreName)
        );

        return encryptor.decrypt(encryptedAccessToken);
    }

    private String getKeyPrefix(String keyPrefix, String arbitraryStoreName) {
        return keyPrefix + currentUserService.getAuthorizedUser().getEmail() + ":" + arbitraryStoreName;
    }
}
