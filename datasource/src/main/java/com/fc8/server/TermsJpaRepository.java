package com.fc8.server;

import com.fc8.platform.domain.entity.terms.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsJpaRepository extends JpaRepository<Terms, Long> {
}
