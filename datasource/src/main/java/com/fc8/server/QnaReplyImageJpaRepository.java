package com.fc8.server;

import com.fc8.platform.domain.entity.qna.QnaReplyImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaReplyImageJpaRepository extends JpaRepository<QnaReplyImage, Long> {
}
