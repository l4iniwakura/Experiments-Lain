package com.github.l4iniwakura.feed.view.counter.repository;

import com.github.l4iniwakura.feed.view.counter.domain.AuthorStatisticKey;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

public class RedisUserClickHitCounterCache implements HitCounterCache<AuthorStatisticKey, UUID>, AutoCloseable {

    private static final Duration TTL = Duration.ofDays(2);

    private final RedisClient redisClient;
    private final StatefulRedisConnection<String, String> connection;
    private final RedisCommands<String, String> commands;

    public RedisUserClickHitCounterCache(RedisClient redisClient) {
        this.redisClient = redisClient;
        this.connection = redisClient.connect();
        this.commands = connection.sync();
    }

    @Override
    public void hit(AuthorStatisticKey authorStatisticKey, UUID userId) {
        String redisKey = toRedisKey(authorStatisticKey);
        commands.pfadd(redisKey, userId.toString());
        commands.expire(redisKey, TTL.getSeconds());
    }

    @Override
    public int getHits(AuthorStatisticKey authorStatisticKey) {
        String redisKey = toRedisKey(authorStatisticKey);
        long count = commands.pfcount(redisKey);

        return (int) count;
    }

    @Override
    public void cleanup() {
        // Redis сам очищает старые ключи по TTL — ничего не нужно
    }

    @Override
    public void registerCleanupJob(ScheduledExecutorService executor) {
        // Cleanup не требуется, можно игнорировать
    }

    @Override
    public void close() {
        connection.close();
        redisClient.shutdown();
    }

    private String toRedisKey(AuthorStatisticKey key) {
        return "clicks:%s:%s".formatted(key.authorId(), key.date());
    }
}