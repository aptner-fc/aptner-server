package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.qna.QQnaComment;
import com.fc8.platform.domain.entity.qna.QQnaReplyImage;
import com.fc8.platform.domain.entity.qna.QnaReplyImage;
import com.fc8.platform.repository.QnaReplyImageRepository;
import com.fc8.server.QnaReplyImageJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QnaReplyImageRepositoryImpl implements QnaReplyImageRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QnaReplyImageJpaRepository qnaReplyImageJpaRepository;

    QQnaComment qnaComment = QQnaComment.qnaComment;
    QQnaReplyImage qnaReplyImage = QQnaReplyImage.qnaReplyImage;

    @Override
    public QnaReplyImage store(QnaReplyImage qnaReplyImage) {
        return qnaReplyImageJpaRepository.save(qnaReplyImage);
    }

    @Override
    public QnaReplyImage getImageByQnaCommentId(Long commentId) {
        QnaReplyImage image = jpaQueryFactory
            .selectFrom(qnaReplyImage)
            .innerJoin(qnaReplyImage.qnaComment, qnaComment)
            .where(
                qnaReplyImage.qnaComment.id.eq(commentId),
                qnaReplyImage.deletedAt.isNull()
            )
            .fetchOne();

        return Optional.ofNullable(image)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT_IMAGE));
    }

}
