package com.github.l4iniwakura.feed.view.counter.service.time;

import java.time.LocalDate;

public interface ITimeService {
    LocalDate today();
    LocalDate yesterday();
}
