package com.github.mirocidij.notification.settings.service;

import com.github.mirocidij.notification.domain.settings.UserNotificationSettings;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface UserNotificationSettingsService {
    UserNotificationSettings getSettings(UUID userId);

    Map<UUID, UserNotificationSettings> getSettingsBatched(Set<UUID> userIdSet);
}
