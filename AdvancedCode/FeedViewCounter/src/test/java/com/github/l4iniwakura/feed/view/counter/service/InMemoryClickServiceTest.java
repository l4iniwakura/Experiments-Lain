package com.github.l4iniwakura.feed.view.counter.service;

import com.github.l4iniwakura.feed.view.counter.core.AuthorStatisticKey;
import com.github.l4iniwakura.feed.view.counter.domain.AuthorMetric;
import com.github.l4iniwakura.feed.view.counter.repository.HitCounterCache;
import com.github.l4iniwakura.feed.view.counter.repository.UserClickHitCounterCache;
import com.github.l4iniwakura.feed.view.counter.service.click.InMemoryClickService;
import com.github.l4iniwakura.feed.view.counter.service.time.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InMemoryClickServiceTest {

    @Mock
    Clock clock;
    HitCounterCache<AuthorStatisticKey, UUID> hitCounterCash;
    TimeService timeService;
    InMemoryClickService iClickService;

    private final Instant baseTime = Instant.parse("2025-01-01T00:00:00Z");

    private final UUID USER_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID USER_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private final UUID USER_3 = UUID.fromString("00000000-0000-0000-0000-000000000003");

    private final UUID AUTHOR_1 = UUID.fromString("10000000-0000-0000-0000-000000000000");
    private final UUID AUTHOR_2 = UUID.fromString("20000000-0000-0000-0000-000000000000");
    private final UUID AUTHOR_3 = UUID.fromString("30000000-0000-0000-0000-000000000000");

    @BeforeEach
    void DefaultSetup() {
        Mockito.when(clock.instant()).thenReturn(baseTime);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
        hitCounterCash = new UserClickHitCounterCache();
        timeService = Mockito.spy(new TimeService(clock));
        iClickService = new InMemoryClickService(timeService, hitCounterCash);
    }

    @Test
    void processClickEventShouldWorkCorrectly() {
        iClickService.processClickEvent(USER_1, AUTHOR_1);
        skipOneDay();
        var expected = new AuthorMetric(Map.of(AUTHOR_1, 1));
        assertEquals(expected, iClickService.calculateMetric(List.of(AUTHOR_1)));
    }

    @Test
    void calculateMethodShouldReturnCorrectStatistic() {
        iClickService.processClickEvent(USER_1, AUTHOR_1);
        iClickService.processClickEvent(USER_2, AUTHOR_1);
        iClickService.processClickEvent(USER_3, AUTHOR_1);
        skipOneDay();
        var expected = new AuthorMetric(Map.of(AUTHOR_1, 3));
        assertEquals(expected, calculateMetric(AUTHOR_1));
    }

    @Test
    void shouldReturnEmptyMetricWhenAuthorListIsEmpty() {
        assertEquals(AuthorMetric.empty(), iClickService.calculateMetric(List.of()));
    }

    @Test
    void processClickIgnoreDuplicates() {
        iClickService.processClickEvent(USER_1, AUTHOR_1);
        iClickService.processClickEvent(USER_1, AUTHOR_1);
        skipOneDay();
        var expected = new AuthorMetric(Map.of(AUTHOR_1, 1));
        assertEquals(expected, calculateMetric(AUTHOR_1));
    }

    @Test
    void multiAuthorStatisticShouldWorkCorrectly() {
        iClickService.processClickEvent(USER_1, AUTHOR_1);
        iClickService.processClickEvent(USER_1, AUTHOR_2);
        iClickService.processClickEvent(USER_1, AUTHOR_3);

        iClickService.processClickEvent(USER_2, AUTHOR_2);
        iClickService.processClickEvent(USER_2, AUTHOR_3);

        iClickService.processClickEvent(USER_3, AUTHOR_3);

        skipOneDay();

        var expected = new AuthorMetric(Map.of(
                AUTHOR_1, 1,
                AUTHOR_2, 2,
                AUTHOR_3, 3
        ));
        assertEquals(expected, calculateMetric(AUTHOR_1, AUTHOR_2, AUTHOR_3));
    }

    private AuthorMetric calculateMetric(UUID... authorIdList) {
        return iClickService.calculateMetric(List.of(authorIdList));
    }

    private void skipOneDay() {
        var nextDay = clock.instant().plus(1, ChronoUnit.DAYS);
        Mockito.when(clock.instant()).thenReturn(nextDay);
    }
}