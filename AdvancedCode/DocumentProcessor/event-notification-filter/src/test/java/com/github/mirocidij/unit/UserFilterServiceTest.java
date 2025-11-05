package com.github.mirocidij.unit;

import com.github.mirocidij.notification.domain.filter.UserNotificationEvent;
import com.github.mirocidij.notification.filter.service.UserNotificationFilterService;
import com.github.mirocidij.notification.filter.service.filter.Filter;
import com.github.mirocidij.notification.filter.service.filter.UserNotificationFilterContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserFilterServiceTest {

    @Mock
    Filter<UserNotificationEvent, UserNotificationFilterContext> firstFilter;

    @Mock
    Filter<UserNotificationEvent, UserNotificationFilterContext> secondFilter;

    private UserNotificationFilterService createUserNotificationFilterService() {
        return new UserNotificationFilterService(List.of(firstFilter, secondFilter));
    }

    private final static UUID userId = UUID.fromString("aa3634d7-e899-46b1-a620-18a889a9a2aa");
    private final static Instant today = Instant.parse("2024-01-15T10:00:00Z");
    private final static Instant yesterday = Instant.parse("2024-01-14T10:00:00Z");

    @Test
    void filterCallOuterServices_whenCallingFilter() {
        var userNotificationFilterService = createUserNotificationFilterService();
        var answer = userNotificationFilterService.filter(
                List.of(), new UserNotificationFilterContext(UUID.randomUUID())
        );
        Assertions.assertNotNull(answer);
    }
}