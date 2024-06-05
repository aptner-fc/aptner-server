package com.fc8.server;

import com.fc8.platform.domain.entity.qna.QnaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaAnswerJpaRepository extends JpaRepository<QnaAnswer, Long> {
}
