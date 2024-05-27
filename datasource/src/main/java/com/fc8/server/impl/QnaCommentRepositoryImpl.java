package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.qna.QQna;
import com.fc8.platform.domain.entity.qna.QQnaComment;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaComment;
import com.fc8.platform.repository.QnaCommentRepository;
import com.fc8.server.QnaCommentJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QnaCommentRepositoryImpl implements QnaCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QnaCommentJpaRepository qnaCommentJpaRepository;

    QQnaComment qnaComment = QQnaComment.qnaComment;
    QQna qqna = QQna.qna;
    QMember member = QMember.member;

    @Override
    public QnaComment getByIdAndQna(Long commentId, Qna qna) {
        QnaComment activeComment = jpaQueryFactory
            .selectFrom(qnaComment)
            .where(
                eqId(commentId),
                eqQna(qna)
            )
            .fetchOne();

        return Optional.ofNullable(activeComment)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    @Override
    public QnaComment store(QnaComment qnaComment) {
        return qnaCommentJpaRepository.save(qnaComment);
    }

    @Override
    public boolean isWriter(QnaComment activeComment, Member loginmember) {
        return jpaQueryFactory
            .selectOne()
            .from(qnaComment)
            .innerJoin(qnaComment.member, member)
            .where(
                qnaComment.eq(activeComment),
                member.eq(loginmember)
            )
            .fetchFirst() != null;
    }

    @Override
    public Page<QnaComment> getCommentListByQna(Long memberId, Qna qna, Pageable pageable, String search) {
        List<QnaComment> commentList = jpaQueryFactory
            .selectFrom(qnaComment)
            .innerJoin(qnaComment.qna, qqna)
            .where(
                qnaComment.qna.eq(qna),
                qnaComment.deletedAt.isNull() // 삭제되지 않은 댓글만 조회
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(qnaComment.count())
            .from(qnaComment)
            .innerJoin(qnaComment.qna, qqna)
            .where(
                qnaComment.qna.eq(qna),
                qnaComment.deletedAt.isNull() // 삭제되지 않은 댓글만 조회
            );

        return PageableExecutionUtils.getPage(commentList, pageable, countQuery::fetchOne);
    }

    private BooleanExpression eqQna(Qna qna) {
        return qnaComment.qna.eq(qna);
    }

    private BooleanExpression eqId(Long id) {
        return qnaComment.id.eq(id);
    }

}
