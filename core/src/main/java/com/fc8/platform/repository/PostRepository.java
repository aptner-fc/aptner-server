package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.post.Post;

public interface PostRepository {

    Post store(Post post);
}
