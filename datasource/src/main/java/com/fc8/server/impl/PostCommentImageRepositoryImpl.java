package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.post.PostCommentImage;
import com.fc8.platform.domain.entity.post.QPostComment;
import com.fc8.platform.domain.entity.post.QPostCommentImage;
import com.fc8.platform.repository.PostCommentImageRepository;
import com.fc8.server.PostCommentImageJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public PostCommentImage getImageByQnaCommentId(Long commentId) {
        PostCommentImage image = jpaQueryFactory
            .selectFrom(postCommentImage)
            .innerJoin(postCommentImage.postComment, postComment)
            .where(
                postCommentImage.postComment.id.eq(commentId),
                postCommentImage.deletedAt.isNull()
            )
            .fetchOne();

        return Optional.ofNullable(image)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT_IMAGE));
    }
}
