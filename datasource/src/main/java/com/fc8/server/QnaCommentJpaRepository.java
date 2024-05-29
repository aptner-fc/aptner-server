package com.fc8.server;

import com.fc8.platform.domain.entity.qna.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaCommentJpaRepository extends JpaRepository<QnaComment, Long> {
}
