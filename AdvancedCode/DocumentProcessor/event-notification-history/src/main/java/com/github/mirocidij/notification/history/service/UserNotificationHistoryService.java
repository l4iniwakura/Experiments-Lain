package com.github.mirocidij.notification.history.service;

import com.github.mirocidij.notification.domain.history.NotificationHistoryEvent;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface UserNotificationHistoryService {
    Set<NotificationHistoryEvent> getHistory(UUID userId, Instant since);

    Map<UUID, Set<NotificationHistoryEvent>> getHistoryBatched(Set<UUID> userId, Instant since);
}
