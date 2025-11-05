package com.github.mirocidij.notification.domain.filter;

import com.github.mirocidij.notification.domain.settings.NotificationChannel;
import lombok.Builder;
import lombok.With;

import java.util.UUID;

@With
@Builder
public record UserNotificationEvent(
        UUID id,
        NotificationChannel notificationType,
        UUID recipientId,
        String text
) {
}
