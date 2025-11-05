package com.github.l4iniwakura.feed.view.counter.config;

import com.github.l4iniwakura.feed.view.counter.core.AuthorStatisticKey;
import com.github.l4iniwakura.feed.view.counter.repository.HitCounterCache;
import com.github.l4iniwakura.feed.view.counter.repository.RedisClickHitCounterCache;
import io.lettuce.core.RedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class CacheConfig {

    @Bean
    public HitCounterCache<AuthorStatisticKey, UUID> redisClickHitCounterCache(RedisClient redisClient) {
        return new RedisClickHitCounterCache(redisClient);
    }
}
