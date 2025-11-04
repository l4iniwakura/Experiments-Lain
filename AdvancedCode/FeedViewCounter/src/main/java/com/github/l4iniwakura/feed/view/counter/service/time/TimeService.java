package com.github.l4iniwakura.feed.view.counter.service.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

public class TimeService implements ITimeService {

    public static final Period ONE_DAY = Period.ofDays(1);
    private final Clock clock;

    public TimeService(final Clock clock) {
        this.clock = clock;
    }

    @Override
    public LocalDate today() {
        return LocalDate.now(clock);
    }

    @Override
    public LocalDate yesterday() {
        return today().minus(ONE_DAY);
    }
}
