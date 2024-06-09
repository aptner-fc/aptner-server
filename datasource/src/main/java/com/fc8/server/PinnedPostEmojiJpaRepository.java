package com.fc8.server;

import com.fc8.platform.domain.entity.pinned.PinnedPostEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinnedPostEmojiJpaRepository extends JpaRepository<PinnedPostEmoji, Long> {
}
