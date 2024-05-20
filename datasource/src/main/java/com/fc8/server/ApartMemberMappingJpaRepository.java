package com.fc8.server;


import com.fc8.platform.domain.entity.mapping.ApartMemberMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartMemberMappingJpaRepository extends JpaRepository<ApartMemberMapping, Long> {
}
