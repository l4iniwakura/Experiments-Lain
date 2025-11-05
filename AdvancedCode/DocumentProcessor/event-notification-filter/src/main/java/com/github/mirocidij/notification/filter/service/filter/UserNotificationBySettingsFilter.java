package com.github.mirocidij.notification.filter.service.filter;

import com.github.mirocidij.notification.domain.filter.UserNotificationEvent;
import com.github.mirocidij.notification.domain.settings.UserNotificationSettings;
import com.github.mirocidij.notification.settings.service.UserNotificationSettingsService;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserNotificationBySettingsFilter implements Filter<UserNotificationEvent, UserNotificationFilterContext> {

    private final UserNotificationSettingsService userNotificationSettingsService;

    public UserNotificationBySettingsFilter(UserNotificationSettingsService userNotificationSettingsService) {
        this.userNotificationSettingsService = userNotificationSettingsService;
    }

    @Override
    public List<UserNotificationEvent> filter(List<UserNotificationEvent> notificationEvents,
                                              UserNotificationFilterContext filterContext) {
        Set<UUID> recipientIdSet = notificationEvents.stream()
                .map(UserNotificationEvent::recipientId)
                .collect(Collectors.toSet());
        var userIdToSettingsMap = userNotificationSettingsService.getSettingsBatched(recipientIdSet);
        var senderId = filterContext.senderId();
        return notificationEvents.stream()
                .filter(e -> {
                    if (userIdToSettingsMap.containsKey(e.recipientId())) {
                        var settings = userIdToSettingsMap.get(e.recipientId());
                        return messageTypePermitted(e, settings) && senderNotBanned(senderId, settings);
                    }

                    return true;
                })
                .toList();
    }

    private static boolean senderNotBanned(UUID senderId, UserNotificationSettings settings) {
        return !settings.bannedSenders().contains(senderId);
    }

    private static boolean messageTypePermitted(UserNotificationEvent e, UserNotificationSettings settings) {
        return settings.allowedChannels().contains(e.notificationType());
    }
}
