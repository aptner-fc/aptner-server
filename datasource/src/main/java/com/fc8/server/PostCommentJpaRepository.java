package com.fc8.server;

import com.fc8.platform.domain.entity.post.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentJpaRepository extends JpaRepository<PostComment, Long> {
}
