package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.apartment.Apart;

public interface ApartRepository {

    Apart getByCode(String code);
}
