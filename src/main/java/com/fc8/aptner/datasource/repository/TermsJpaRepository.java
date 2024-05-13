package com.fc8.aptner.datasource.repository;

import com.fc8.aptner.core.domain.entity.terms.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsJpaRepository extends JpaRepository<Terms, Long> {
}
