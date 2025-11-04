package com.github.l4iniwakura.feed.view.counter.repository;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Потокобезопасный кеш для подсчёта уникальных значений VALUE на ключ KEY.
 * Например: подсчёт уникальных пользователей на автора за день.
 */
public interface HitCounterCache<KEY, VALUE> {
    void hit(KEY key, VALUE value);

    int getHits(KEY key);

    void cleanup();

    default void registerCleanupJob(ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorService.scheduleAtFixedRate(this::cleanup, 1, 1, TimeUnit.DAYS);
    }
}
