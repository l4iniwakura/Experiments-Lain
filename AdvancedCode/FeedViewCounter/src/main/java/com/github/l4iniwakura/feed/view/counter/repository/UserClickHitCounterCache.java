package com.github.l4iniwakura.feed.view.counter.repository;

import com.github.l4iniwakura.feed.view.counter.core.AuthorStatisticKey;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

@ThreadSafe
public class UserClickHitCounterCache implements HitCounterCache<AuthorStatisticKey, UUID> {

    private final ConcurrentMap<AuthorStatisticKey, Set<UUID>> cache;
    private final Predicate<AuthorStatisticKey> cleanupPredicate;

    public UserClickHitCounterCache() {
        this(null);
    }

    public UserClickHitCounterCache(Predicate<AuthorStatisticKey> cleanupPredicate) {
        this.cache = new ConcurrentHashMap<>();
        this.cleanupPredicate = Objects.requireNonNullElse(cleanupPredicate,
                _ -> false);
    }

    @Override
    public void hit(AuthorStatisticKey key, UUID value) {
        cache.computeIfAbsent(key, _ -> ConcurrentHashMap.newKeySet()).add(value);
    }

    @Override
    public int getHits(AuthorStatisticKey key) {
        return cache.getOrDefault(key, Collections.emptySet()).size();
    }

    @Override
    public void cleanup() {
        cache.keySet().removeIf(cleanupPredicate);
    }
}
