package com.fc8.server.impl;

import com.fc8.platform.domain.entity.notification.NotificationHistory;
import com.fc8.platform.repository.NotificationHistoryRepository;
import com.fc8.server.NotificationHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationHistoryRepositoryImpl implements NotificationHistoryRepository {

    private final NotificationHistoryJpaRepository notificationHistoryJpaRepository;

    @Override
    public NotificationHistory store(NotificationHistory notificationHistory) {
        return notificationHistoryJpaRepository.save(notificationHistory);
    }

    @Override
    public void delete(NotificationHistory notificationHistory) {
        notificationHistoryJpaRepository.delete(notificationHistory);
    }
}
