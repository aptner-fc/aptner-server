package com.fc8.server;

import com.fc8.platform.domain.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
