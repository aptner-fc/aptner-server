package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.alert.Alert;

public interface AlertRepository {

    Alert store(Alert alert);

    void delete(Alert alert);

}
