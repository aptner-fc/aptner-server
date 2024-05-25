package com.fc8.server.impl;

import com.fc8.platform.domain.entity.qna.QQnaComment;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaComment;
import com.fc8.platform.repository.QnaCommentRepository;
import com.fc8.server.QnaCommentJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QnaCommentRepositoryImpl implements QnaCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QnaCommentJpaRepository qnaCommentJpaRepository;

    QQnaComment qnaComment = QQnaComment.qnaComment;

    @Override
    public QnaComment getByIdAndQna(Long id, Qna qna) {
        return jpaQueryFactory
            .selectFrom(qnaComment)
            .where(
                eqId(id),
                eqQna(qna),
                isNotDeleted()
            )
            .fetchOne();
    }

    @Override
    public QnaComment store(QnaComment qnaComment) {
        return qnaCommentJpaRepository.save(qnaComment);
    }

    private BooleanExpression isNotDeleted() {
        return qnaComment.deletedAt.isNull();
    }

    private BooleanExpression eqQna(Qna qna) {
        return qnaComment.qna.eq(qna);
    }

    private BooleanExpression eqId(Long id) {
        return qnaComment.id.eq(id);
    }

}
