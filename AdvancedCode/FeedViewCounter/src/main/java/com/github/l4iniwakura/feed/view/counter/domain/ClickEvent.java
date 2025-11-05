package com.github.l4iniwakura.feed.view.counter.domain;

import java.util.UUID;

public record ClickEvent(UUID userId, UUID authorId) {
    public static ClickEvent of(UUID userId, UUID authorId) {
        return new ClickEvent(userId, authorId);
    }
}
