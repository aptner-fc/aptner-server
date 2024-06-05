package com.fc8.server.impl;

import com.fc8.platform.domain.entity.notification.Notification;
import com.fc8.platform.repository.NotificationRepository;
import com.fc8.server.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public Notification store(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    @Override
    public void delete(Notification notification) {
        notificationJpaRepository.delete(notification);
    }
}
