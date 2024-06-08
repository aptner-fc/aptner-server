package com.fc8.server.impl;

import com.fc8.platform.domain.entity.post.PostCommentImage;
import com.fc8.platform.domain.entity.post.QPostComment;
import com.fc8.platform.domain.entity.post.QPostCommentImage;
import com.fc8.platform.repository.PostCommentImageRepository;
import com.fc8.server.PostCommentImageJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCommentImageRepositoryImpl implements PostCommentImageRepository {

    private final PostCommentImageJpaRepository postCommentImageJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    QPostCommentImage postCommentImage = QPostCommentImage.postCommentImage;
    QPostComment postComment = QPostComment.postComment;


    @Override
    public PostCommentImage store(PostCommentImage postCommentImage) {
        return postCommentImageJpaRepository.save(postCommentImage);
    }

    @Override
    public PostCommentImage getByPostCommentId(Long commentId) {
        return jpaQueryFactory
            .selectFrom(postCommentImage)
            .innerJoin(postCommentImage.postComment, postComment)
            .where(
                postCommentImage.postComment.id.eq(commentId),
                postCommentImage.deletedAt.isNull()
            )
            .fetchOne();
    }

}
