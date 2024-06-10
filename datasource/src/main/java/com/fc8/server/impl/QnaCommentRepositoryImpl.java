package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.member.QMemberBlock;
import com.fc8.platform.domain.entity.qna.*;
import com.fc8.platform.dto.record.QnaCommentInfo;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.QnaCommentRepository;
import com.fc8.server.QnaCommentJpaRepository;
import com.querydsl.core.types.Projections;
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
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class QnaCommentRepositoryImpl implements QnaCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QnaCommentJpaRepository qnaCommentJpaRepository;

    QQnaCommentImage qnaCommentImage = QQnaCommentImage.qnaCommentImage;
    QMember member = QMember.member;
    QMemberBlock memberBlock = QMemberBlock.memberBlock;
    QQna qna = QQna.qna;
    QQnaComment qnaComment = QQnaComment.qnaComment;

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
    public Page<QnaCommentInfo> getCommentListByQna(Long qnaId, Pageable pageable) {
        List<QnaCommentInfo> commentList = jpaQueryFactory
            .select(Projections.constructor(
                QnaCommentInfo.class,
                qnaComment.id,
                qnaComment.parent.id,
                qnaComment.content,
                qnaComment.createdAt,
                qnaComment.updatedAt,
                qnaCommentImage.imagePath.as("imageUrl"),
                Projections.constructor(
                    WriterInfo.class,
                    qnaComment.member.id,
                    qnaComment.member.name,
                    qnaComment.member.nickname
                )
            ))
            .from(qnaComment)
            .innerJoin(qna).on(qnaComment.qna.id.eq(qna.id))
            .leftJoin(qnaCommentImage).on(qnaCommentImage.qnaComment.id.eq(qnaComment.id))
            .where(
                qnaComment.qna.id.eq(qnaId),
                qnaComment.deletedAt.isNull(), // 삭제되지 않은 댓글만 조회
                qnaCommentImage.deletedAt.isNull()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(qnaComment.count())
            .from(qnaComment)
            .innerJoin(qnaComment.qna, this.qna)
            .where(
                qnaComment.qna.eq(qna),
                qnaComment.deletedAt.isNull() // 삭제되지 않은 댓글만 조회
            );

        return PageableExecutionUtils.getPage(commentList, pageable, countQuery::fetchOne);
    }

    @Override
    public QnaComment getByIdAndQnaIdAndMemberId(Long id, Long qnaId, Long memberId) {
        QnaComment writtenComment = jpaQueryFactory
            .selectFrom(qnaComment)
            .innerJoin(qna).on(qnaComment.qna.id.eq(qna.id))
            .innerJoin(member).on(qnaComment.member.id.eq(member.id))
            .where(
                eqId(id),
                eqQnaCommentQna(qnaComment.qna, qnaId),
                eqQnaCommentWriter(qnaComment.member, memberId),
                isNotDeleted()
            )
            .fetchOne();

        return Optional.ofNullable(writtenComment)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    @Override
    public List<QnaComment> getAllByIdsAndMember(List<Long> qnaCommentIds, Member activeMember) {
        return jpaQueryFactory
                .selectFrom(qnaComment)
                .innerJoin(qna).on(qnaComment.qna.id.eq(qna.id))
                .innerJoin(member).on(qnaComment.member.id.eq(member.id))
                .where(
                        isNotDeleted(qna),
                        isNotDeleted(),
                        eqQnaCommentWriter(qnaComment.member, activeMember.getId()),
                        qnaComment.id.in(qnaCommentIds)
                )
                .fetch();
    }

    @Override
    public Page<QnaComment> getAllByQnaIdAndMemberId(Long qnaId, Long memberId, Pageable pageable) {
        QQnaComment parent = new QQnaComment("parent");
        List<QnaComment> commentList = jpaQueryFactory
                .selectFrom(qnaComment)
                .innerJoin(qna).on(qnaComment.qna.eq(qna))
                .leftJoin(parent).on(qnaComment.parent.eq(parent))
                .where(
                        qna.id.eq(qnaId),
                        qna.deletedAt.isNull()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qnaComment.createdAt.asc())
                .fetch();

        List<Long> blockedOrBlockingMemberIds = getBlockedOrBlockingMemberIds(memberId);

        // 차단된 댓글 여부 설정
        commentList.forEach(comment -> {
            if (blockedOrBlockingMemberIds.contains(comment.getMember().getId())) {
                comment.changeBlockStatus(true);
            }
        });

        JPAQuery<Long> count = jpaQueryFactory
                .select(qnaComment.count())
                .from(qnaComment)
                .innerJoin(qna).on(qnaComment.qna.eq(qna))
                .where(
                        qna.id.eq(qnaId),
                        qna.deletedAt.isNull()
                );

        return PageableExecutionUtils.getPage(commentList, pageable, count::fetchOne);
    }

    private BooleanExpression eqQnaCommentWriter(QMember member, Long memberId) {
        return member.id.eq(memberId);
    }

    private BooleanExpression eqQnaCommentQna(QQna qna, Long qnaId) {
        return qna.id.eq(qnaId);
    }

    private BooleanExpression eqQna(Qna qna) {
        return qnaComment.qna.eq(qna);
    }

    private BooleanExpression eqId(Long id) {
        return qnaComment.id.eq(id);
    }

    private BooleanExpression isNotDeleted() {
        return qnaComment.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted(QQna qna) {
        return qna.deletedAt.isNull();
    }

    private List<Long> getBlockedOrBlockingMemberIds(Long memberId) {
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        return Stream.concat(blockedMemberIds.stream(), blockingMemberIds.stream())
                .distinct()
                .toList();
    }


    private List<Long> getBlockedMemberIds(Long memberId) {
        return jpaQueryFactory
                .select(memberBlock.blocked.id)
                .from(memberBlock)
                .where(memberBlock.member.id.eq(memberId))
                .fetch();
    }

    private List<Long> getBlockingMemberIds(Long memberId) {
        return jpaQueryFactory
                .select(memberBlock.member.id)
                .from(memberBlock)
                .where(memberBlock.blocked.id.eq(memberId))
                .fetch();
    }

}
