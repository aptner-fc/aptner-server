package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.post.PostCommentImage;

public interface PostCommentImageRepository {

    PostCommentImage store(PostCommentImage postCommentImage);

    PostCommentImage getByPostCommentId(Long commentId);
}
