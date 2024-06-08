package com.fc8.server;

import com.fc8.platform.domain.entity.pinned.PinnedPostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinnedPostCommentJpaRepository extends JpaRepository<PinnedPostComment, Long> {
}
