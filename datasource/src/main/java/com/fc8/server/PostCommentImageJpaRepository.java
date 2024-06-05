package com.fc8.server;

import com.fc8.platform.domain.entity.post.PostCommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentImageJpaRepository extends JpaRepository<PostCommentImage, Long> {
}
