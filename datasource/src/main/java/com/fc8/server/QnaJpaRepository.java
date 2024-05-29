package com.fc8.server;

import com.fc8.platform.domain.entity.qna.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaJpaRepository extends JpaRepository<Qna, Long> {
}
