package com.fc8.server;

import com.fc8.platform.domain.entity.mapping.NotificationMemberMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationMemberMappingJpaRepository extends JpaRepository<NotificationMemberMapping, Long> {
}
