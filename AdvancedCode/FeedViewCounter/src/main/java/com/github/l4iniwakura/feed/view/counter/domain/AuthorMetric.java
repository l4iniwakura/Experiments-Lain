package com.github.l4iniwakura.feed.view.counter.domain;

import java.util.Map;
import java.util.UUID;

public record AuthorMetric(
        Map<UUID, Integer> authorToClick
) {
    public static AuthorMetric empty() {
        return new AuthorMetric(Map.of());
    }

    @Override
    public String toString() {
        return "AuthorMetric{" +
                "authorToClick=" + authorToClick +
                '}';
    }
}
