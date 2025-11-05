package com.github.mirocidij.notification.filter.service;

import com.github.mirocidij.notification.domain.filter.UserNotificationEvent;
import com.github.mirocidij.notification.filter.service.filter.Filter;
import com.github.mirocidij.notification.filter.service.filter.UserNotificationFilterContext;

import java.util.List;

public class UserNotificationFilterService implements Filter<UserNotificationEvent, UserNotificationFilterContext> {

    private final List<Filter<UserNotificationEvent, UserNotificationFilterContext>> filters;

    public UserNotificationFilterService(List<Filter<UserNotificationEvent, UserNotificationFilterContext>> filters) {
        this.filters = filters;
    }

    @Override
    public List<UserNotificationEvent> filter(List<UserNotificationEvent> userNotificationEvents,
                                              UserNotificationFilterContext filterContext) {
        var filtered = userNotificationEvents;
        for (var filter : filters) {
            filtered = filter.filter(filtered, filterContext);
        }
        return filtered;
    }
}
