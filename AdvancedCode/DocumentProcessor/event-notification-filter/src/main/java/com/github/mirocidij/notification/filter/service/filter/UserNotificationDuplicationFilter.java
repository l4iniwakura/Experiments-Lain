package com.github.mirocidij.notification.filter.service.filter;

import com.github.mirocidij.notification.domain.filter.UserNotificationEvent;
import com.github.mirocidij.notification.domain.history.NotificationHistoryEvent;
import com.github.mirocidij.notification.history.service.UserNotificationHistoryService;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserNotificationDuplicationFilter implements Filter<UserNotificationEvent, UserNotificationFilterContext> {

    private final UserNotificationHistoryService userNotificationHistoryService;
    private final Clock clock;

    public UserNotificationDuplicationFilter(UserNotificationHistoryService userNotificationHistoryService, Clock clock) {
        this.userNotificationHistoryService = userNotificationHistoryService;
        this.clock = clock;
    }

    @Override
    public List<UserNotificationEvent> filter(List<UserNotificationEvent> notificationEvents,
                                              UserNotificationFilterContext filterContext) {
        var sinceYesterday = Instant.now(clock).minus(1, ChronoUnit.DAYS);
        Set<UUID> recipientIdSet = notificationEvents.stream()
                .map(UserNotificationEvent::recipientId)
                .collect(Collectors.toSet());
        var userToNotificationHistory = userNotificationHistoryService.getHistoryBatched(recipientIdSet, sinceYesterday);

        return notificationEvents.stream()
                .filter(e -> !hasNotificationInHistory(e, userToNotificationHistory))
                .toList();
    }

    private static boolean hasNotificationInHistory(UserNotificationEvent e, Map<UUID, Set<NotificationHistoryEvent>> userToNotificationHistory) {
        return userToNotificationHistory.getOrDefault(e.recipientId(), Set.of())
                .stream()
                .anyMatch(event -> event.notificationId().equals(e.id()));
    }
}
