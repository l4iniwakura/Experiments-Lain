package com.github.l4iniwakura.feed.view.counter.repository;

import com.github.l4iniwakura.feed.view.counter.AbstractIntegrationTest;
import com.github.l4iniwakura.feed.view.counter.core.AuthorStatisticKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class RedisClickHitCounterCacheTest extends AbstractIntegrationTest {

    private final static UUID AUTHOR_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final static UUID USER_1 = UUID.fromString("10000000-0000-0000-0000-000000000000");

    private static HitCounterCache<AuthorStatisticKey, UUID> cache;

    @BeforeEach
    void initCache() {
        cache = new RedisClickHitCounterCache(redisClient());
    }

    @Test
    void shouldReturnApproximatelyExactValue_WhenGetHist() {
        var key = new AuthorStatisticKey(AUTHOR_1, LocalDate.now());
        for (int i = 0; i < 1000; i++) {
            cache.hit(key, UUID.randomUUID());
        }
        approximateEquals(1000, cache.getHits(key));
    }

    @Test
    void shouldNotCountDuplicates() {
        var key = new AuthorStatisticKey(AUTHOR_1, LocalDate.now());
        for (int i = 0; i < 1000; i++) {
            cache.hit(key, USER_1);
        }
        approximateEquals(1, cache.getHits(key));
    }

    private void approximateEquals(int expected, int fact) {
        var threshold = BigDecimal.ONE;
        var oneHundred = new BigDecimal(100);
        var factBD = new BigDecimal(fact);
        var expectedBG = new BigDecimal(expected);
        var onePercent = expectedBG.multiply(threshold)
                .divide(oneHundred, 2, RoundingMode.HALF_UP);


        var result = factBD.compareTo(expectedBG.subtract(onePercent)) > 0
                && factBD.compareTo(expectedBG.add(onePercent)) < 0;

        assertTrue(result);
    }
}