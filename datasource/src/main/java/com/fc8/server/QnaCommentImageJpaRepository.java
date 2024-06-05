package com.fc8.server;

import com.fc8.platform.domain.entity.qna.QnaCommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaCommentImageJpaRepository extends JpaRepository<QnaCommentImage, Long> {
}
