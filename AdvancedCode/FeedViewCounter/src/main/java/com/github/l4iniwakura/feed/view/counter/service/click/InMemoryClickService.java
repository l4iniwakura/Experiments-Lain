package com.github.l4iniwakura.feed.view.counter.service.click;

import com.github.l4iniwakura.feed.view.counter.core.AuthorStatisticKey;
import com.github.l4iniwakura.feed.view.counter.domain.AuthorMetric;
import com.github.l4iniwakura.feed.view.counter.repository.HitCounterCache;
import com.github.l4iniwakura.feed.view.counter.service.time.ITimeService;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryClickService implements IClickService {

    private final ITimeService timeService;
    private final HitCounterCache<AuthorStatisticKey, UUID> userClickCache;

    public InMemoryClickService(
            final ITimeService timeService,
            final HitCounterCache<AuthorStatisticKey, UUID> hitCounterCache
    ) {
        this.timeService = timeService;
        this.userClickCache = hitCounterCache;
    }

    @Override
    public void processClickEvent(UUID userId, UUID authorId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(authorId);

        var key = new AuthorStatisticKey(authorId, timeService.today());
        userClickCache.hit(key, userId);
    }

    @Override
    public AuthorMetric calculateMetric(List<UUID> authorIdList) {
        if (authorIdList == null || authorIdList.isEmpty()) {
            return AuthorMetric.empty();
        }
        var yesterday = timeService.yesterday();
        var metrics = authorIdList.stream()
                .collect(Collectors.toMap(
                        authorId -> authorId,
                        authorId -> userClickCache.getHits(new AuthorStatisticKey(authorId, yesterday))
                ));
        return new AuthorMetric(metrics);
    }
}
