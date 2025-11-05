package com.github.mirocidij.notification.filter.service.filter;

import java.util.UUID;

public record UserNotificationFilterContext(
        UUID senderId
) {
}
