package com.github.l4iniwakura.feed.view.counter.repository;

import com.github.l4iniwakura.feed.view.counter.core.AuthorStatisticKey;
import com.redis.testcontainers.RedisContainer;
import io.lettuce.core.RedisClient;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class RedisClickHitCounterCacheTest {

    @Container
    private static RedisContainer redisContainer = new RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));

    private static UUID AUTHOR_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private HitCounterCache<AuthorStatisticKey, UUID> cache;

    @Test
    void test() throws InterruptedException {
        String redisUri = redisContainer.getRedisURI();
        try (
                RedisClient redisClient = RedisClient.create(redisUri)
        ) {
            cache = new RedisClickHitCounterCache(redisClient);
            var key = new AuthorStatisticKey(AUTHOR_1, LocalDate.now());
            for (int i = 0; i < 1000; i++) {
                cache.hit(key, UUID.randomUUID());
            }
            assertEquals(1, cache.getHits(key));
        }

        Thread.sleep(2000);
    }

    @Test
    void test2() throws InterruptedException {
        String redisUri = redisContainer.getRedisURI();
        try (
                RedisClient redisClient = RedisClient.create(redisUri)
        ) {
            cache = new RedisClickHitCounterCache(redisClient);
            var key = new AuthorStatisticKey(AUTHOR_1, LocalDate.now());
            cache.hit(key, UUID.randomUUID());
            assertEquals(1, cache.getHits(key));
        }

        Thread.sleep(2000);
    }

    @Test
    void test3() throws InterruptedException {
        String redisUri = redisContainer.getRedisURI();
        try (
                RedisClient redisClient = RedisClient.create(redisUri)
        ) {
            cache = new RedisClickHitCounterCache(redisClient);
            var key = new AuthorStatisticKey(AUTHOR_1, LocalDate.now());
            cache.hit(key, UUID.randomUUID());
            assertEquals(1, cache.getHits(key));
        }

        Thread.sleep(2000);
    }

    @Test
    void test4() throws InterruptedException {
        String redisUri = redisContainer.getRedisURI();
        try (
                RedisClient redisClient = RedisClient.create(redisUri)
        ) {
            cache = new RedisClickHitCounterCache(redisClient);
            var key = new AuthorStatisticKey(AUTHOR_1, LocalDate.now());
            cache.hit(key, UUID.randomUUID());
            assertEquals(1, cache.getHits(key));
        }

        Thread.sleep(2000);
    }

}