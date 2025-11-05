package com.github.l4iniwakura.feed.view.counter;

import com.redis.testcontainers.RedisContainer;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class AbstractIntegrationTest {

    @Container
    private final static RedisContainer redisContainer = new RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;
    private static RedisCommands<String, String> commands;

    protected String getRedisURI() {
        return redisContainer.getRedisURI();
    }

    protected static RedisClient redisClient() {
        return redisClient;
    }

    @BeforeAll
    static void setup() {
        redisClient = RedisClient.create(redisContainer.getRedisURI());
        connection = redisClient.connect();
        commands = connection.sync();
    }

    @BeforeEach
    void redisCleanup() {
        commands.flushall();
    }

    @AfterAll
    static void closeClient() {
        connection.close();
        redisClient.shutdown();
    }
}
