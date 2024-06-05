package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.qna.QQnaComment;
import com.fc8.platform.domain.entity.qna.QQnaCommentImage;
import com.fc8.platform.domain.entity.qna.QnaCommentImage;
import com.fc8.platform.repository.QnaCommentImageRepository;
import com.fc8.server.QnaCommentImageJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QnaCommentImageRepositoryImpl implements QnaCommentImageRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QnaCommentImageJpaRepository qnaCommentImageJpaRepository;

    QQnaComment qnaComment = QQnaComment.qnaComment;
    QQnaCommentImage qnaCommentImage = QQnaCommentImage.qnaCommentImage;

    @Override
    public QnaCommentImage store(QnaCommentImage qnaCommentImage) {
        return qnaCommentImageJpaRepository.save(qnaCommentImage);
    }

    @Override
    public QnaCommentImage getImageByQnaCommentId(Long commentId) {
        QnaCommentImage image = jpaQueryFactory
            .selectFrom(qnaCommentImage)
            .innerJoin(qnaCommentImage.qnaComment, qnaComment)
            .where(
                qnaCommentImage.qnaComment.id.eq(commentId),
                qnaCommentImage.deletedAt.isNull()
            )
            .fetchOne();

        return Optional.ofNullable(image)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT_IMAGE));
    }

}
