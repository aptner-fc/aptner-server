package com.fc8.server.impl;

import com.fc8.platform.domain.entity.alert.Alert;
import com.fc8.platform.repository.AlertRepository;
import com.fc8.server.AlertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlertRepositoryImpl implements AlertRepository {

    private final AlertJpaRepository alertJpaRepository;

    @Override
    public Alert store(Alert alert) {
        return alertJpaRepository.save(alert);
    }

    @Override
    public void delete(Alert alert) {
        alertJpaRepository.delete(alert);
    }
}
