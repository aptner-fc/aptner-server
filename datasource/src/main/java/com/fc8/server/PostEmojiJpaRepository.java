package com.fc8.server;

import com.fc8.platform.domain.entity.post.PostEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEmojiJpaRepository extends JpaRepository<PostEmoji, Long> {
}