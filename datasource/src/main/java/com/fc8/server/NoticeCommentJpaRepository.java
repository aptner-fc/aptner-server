package com.fc8.server;

import com.fc8.platform.domain.entity.notice.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeCommentJpaRepository extends JpaRepository<NoticeComment, Long> {
}
