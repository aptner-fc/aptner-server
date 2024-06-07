package com.fc8.server;

import com.fc8.platform.domain.entity.notification.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationHistoryJpaRepository extends JpaRepository<NotificationHistory, Long> {
}
