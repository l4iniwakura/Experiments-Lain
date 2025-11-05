package com.github.l4iniwakura.feed.view.counter.service.click;

import com.github.l4iniwakura.feed.view.counter.common.AuthorMetric;
import com.github.l4iniwakura.feed.view.counter.common.ClickEvent;

import java.util.List;
import java.util.UUID;

public interface IClickService {
    void processClickEvent(final ClickEvent clickEvent);
    AuthorMetric calculateMetric(List<UUID> authrIdList);
}
