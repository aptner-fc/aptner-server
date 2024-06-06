package com.fc8.server;

import com.fc8.platform.domain.entity.apartment.ApartArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartAreaJpaRepository extends JpaRepository<ApartArea, Long> {
}
