package com.fc8.server;


import com.fc8.platform.domain.entity.mapping.TermsMemberMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsMemberMappingJpaRepository extends JpaRepository<TermsMemberMapping, Long> {
}
