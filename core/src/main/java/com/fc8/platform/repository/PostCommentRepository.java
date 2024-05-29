package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostComment;

public interface PostCommentRepository {

    PostComment getByIdAndPost(Long id, Post post);

    PostComment store(PostComment postComment);

    PostComment getByIdAndPostIdAndMemberId(Long id, Long postId, Long memberId);
}
