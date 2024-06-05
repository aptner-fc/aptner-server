package com.fc8.server;

import com.fc8.platform.domain.entity.notice.NoticeEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeEmojiJpaRepository extends JpaRepository<NoticeEmoji, Long> {
}
