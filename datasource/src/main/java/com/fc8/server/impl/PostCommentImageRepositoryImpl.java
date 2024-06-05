package com.fc8.server.impl;

import com.fc8.platform.domain.entity.post.PostCommentImage;
import com.fc8.platform.repository.PostCommentImageRepository;
import com.fc8.server.PostCommentImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCommentImageRepositoryImpl implements PostCommentImageRepository {

    private final PostCommentImageJpaRepository postCommentImageJpaRepository;


    @Override
    public PostCommentImage store(PostCommentImage postCommentImage) {
        return postCommentImageJpaRepository.save(postCommentImage);
    }
}
