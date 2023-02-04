package com.antevorta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisClientConfig;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.password}")
    private String password;

    @Bean
    public Jedis redisClient(JedisClientConfig jedisClientConfig) {
        return new Jedis(host, port, jedisClientConfig);
    }

    @Bean
    public JedisClientConfig jedisClientConfig() {
        return DefaultJedisClientConfig.builder()
                .password(password)
                .build();
    }
}
