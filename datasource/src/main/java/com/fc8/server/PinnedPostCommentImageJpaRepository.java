package com.fc8.server;

import com.fc8.platform.domain.entity.pinned.PinnedPostCommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinnedPostCommentImageJpaRepository extends JpaRepository<PinnedPostCommentImage, Long> {
}
