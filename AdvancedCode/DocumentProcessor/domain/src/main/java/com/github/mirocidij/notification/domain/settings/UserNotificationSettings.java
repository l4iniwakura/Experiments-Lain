package com.github.mirocidij.notification.domain.settings;

import lombok.Builder;
import lombok.With;

import java.util.Set;
import java.util.UUID;

@With
@Builder
public record UserNotificationSettings(
        Set<NotificationChannel> allowedChannels,
        Set<UUID> bannedSenders
) {
}
