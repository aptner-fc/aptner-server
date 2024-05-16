package com.fc8.server;

import com.fc8.platform.domain.entity.apartment.Apart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApartJpaRepository extends JpaRepository<Apart, Long> {

    Optional<Apart> findByCodeAndIsUsedTrue(String code);
}
