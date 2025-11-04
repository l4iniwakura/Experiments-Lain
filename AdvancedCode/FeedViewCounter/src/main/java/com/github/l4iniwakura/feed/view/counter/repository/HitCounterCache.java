package com.github.l4iniwakura.feed.view.counter.repository;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface HitCounterCache<KEY, VALUE> {
    void hit(KEY key, VALUE value);

    Set<VALUE> get(KEY key);

    void cleanup();

    default void registerCleanupJob(ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorService.scheduleAtFixedRate(this::cleanup, 1, 1, TimeUnit.DAYS);
    }
}
