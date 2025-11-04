package com.github.l4iniwakura.feed.view.counter.service.click;

import com.github.l4iniwakura.feed.view.counter.domain.AuthorMetric;

import java.util.List;
import java.util.UUID;

public interface IClickService {
    void processClickEvent(UUID userId, UUID authorId);
    AuthorMetric calculateMetric(List<UUID> authrIdList);
}
