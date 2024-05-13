package com.fc8.aptner.datasource.repository;

import com.fc8.aptner.core.domain.entity.history.TermsMemberMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsMemberMappingJpaRepository extends JpaRepository<TermsMemberMapping, Long> {
}
