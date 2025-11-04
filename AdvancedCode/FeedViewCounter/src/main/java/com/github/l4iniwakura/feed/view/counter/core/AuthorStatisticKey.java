package com.github.l4iniwakura.feed.view.counter.core;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorStatisticKey(UUID authorId, LocalDate date) {
}
