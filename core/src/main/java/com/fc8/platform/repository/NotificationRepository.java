package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.notification.Notification;

public interface NotificationRepository {

    Notification store(Notification notification);

    void delete(Notification notification);

}
