package com.github.mirocidij.notification.filter.service.filter;

import java.util.List;

public interface Filter<T, CTX> {

    List<T> filter(List<T> notificationEvents, CTX filterContext);

}
