package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository {

    Post store(Post post);

    Page<Post> getPostListByApartCode(Long memberId, String apartCode, Pageable pageable, String search);

    Post getByIdAndApartCode(Long postId, String apartCode);

    Post getPostWithCategoryByIdAndApartCode(Long postId, String apartCode);
}
