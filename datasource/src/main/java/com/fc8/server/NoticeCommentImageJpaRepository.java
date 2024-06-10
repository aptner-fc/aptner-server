package com.fc8.server;

import com.fc8.platform.domain.entity.notice.NoticeCommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeCommentImageJpaRepository extends JpaRepository<NoticeCommentImage, Long> {
}
