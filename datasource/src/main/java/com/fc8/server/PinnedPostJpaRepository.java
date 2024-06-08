package com.fc8.server;

import com.fc8.platform.domain.entity.pinned.PinnedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinnedPostJpaRepository extends JpaRepository<PinnedPost, Long> {
}
