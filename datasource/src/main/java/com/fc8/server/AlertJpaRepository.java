package com.fc8.server;

import com.fc8.platform.domain.entity.alert.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertJpaRepository extends JpaRepository<Alert, Long> {
}
