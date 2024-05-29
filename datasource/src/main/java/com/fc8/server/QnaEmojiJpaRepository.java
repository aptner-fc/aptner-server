package com.fc8.server;

import com.fc8.platform.domain.entity.qna.QnaEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaEmojiJpaRepository extends JpaRepository<QnaEmoji, Long> {
}
