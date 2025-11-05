package com.github.mirocidij.notification.domain.history;

import lombok.Builder;
import lombok.With;

import java.util.UUID;

@With
@Builder
public record NotificationHistoryEvent(
        UUID notificationId
) {
}
