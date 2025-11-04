package com.github.l4iniwakura.feed.view.counter.service.click;

import com.github.l4iniwakura.feed.view.counter.core.AuthorStatisticKey;
import com.github.l4iniwakura.feed.view.counter.domain.AuthorMetric;
import com.github.l4iniwakura.feed.view.counter.repository.HitCounterCache;
import com.github.l4iniwakura.feed.view.counter.service.time.ITimeService;

import java.util.*;

public class InMemoryClickService implements IClickService {

    private final ITimeService timeService;
    private final HitCounterCache<AuthorStatisticKey, UUID> userClickCash;

    public InMemoryClickService(
            final ITimeService timeService,
            final HitCounterCache<AuthorStatisticKey, UUID> hitCounterCache
    ) {
        this.timeService = timeService;
        this.userClickCash = hitCounterCache;
    }

    @Override
    public void processClickEvent(UUID userId, UUID authorId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(authorId);

        var key = new AuthorStatisticKey(authorId, timeService.today());
        userClickCash.hit(key, userId);
    }

    @Override
    public AuthorMetric calculateMetric(List<UUID> authorIdList) {
        Objects.requireNonNull(authorIdList);

        var yesterday = timeService.yesterday();
        Map<UUID, Integer> metrics = new HashMap<>(authorIdList.size());
        for (UUID authorId : authorIdList) {
            var key = new AuthorStatisticKey(authorId, yesterday);
            var clicks = userClickCash.get(key).size();
            metrics.put(authorId, clicks);
        }
        return new AuthorMetric(metrics);
    }
}
