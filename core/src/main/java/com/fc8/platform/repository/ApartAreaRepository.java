package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.apartment.ApartArea;

import java.util.List;

public interface ApartAreaRepository {

    List<ApartArea> getAllByApartCode(String apartCode);

    ApartArea getById(Long id);
}
