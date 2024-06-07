package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.notification.NotificationHistory;

public interface NotificationHistoryRepository {

    NotificationHistory store(NotificationHistory notificationHistory);

    void delete(NotificationHistory notificationHistory);

}
