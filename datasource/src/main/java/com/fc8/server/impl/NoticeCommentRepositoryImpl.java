package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.notice.*;
import com.fc8.platform.dto.record.NoticeCommentInfo;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.NoticeCommentRepository;
import com.fc8.server.NoticeCommentJpaRepository;
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

@Repository
@RequiredArgsConstructor
public class NoticeCommentRepositoryImpl implements NoticeCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final NoticeCommentJpaRepository noticeCommentJpaRepository;

    QNoticeComment noticeComment = QNoticeComment.noticeComment;
    QNotice notice = QNotice.notice;
    QMember member = QMember.member;
    QNoticeCommentImage noticeCommentImage = QNoticeCommentImage.noticeCommentImage;

    @Override
    public Page<NoticeCommentInfo> getCommentListByQna(Long noticeId, Pageable pageable) {
        List<NoticeCommentInfo> commentList = jpaQueryFactory
            .select(Projections.constructor(
                NoticeCommentInfo.class,
                noticeComment.id,
                noticeComment.parent.id,
                noticeComment.content,
                noticeComment.createdAt,
                noticeComment.updatedAt,
                noticeCommentImage.imagePath.as("imageUrl"),
                Projections.constructor(
                    WriterInfo.class,
                    noticeComment.member.id,
                    noticeComment.member.name,
                    noticeComment.member.nickname
                )
            ))
            .from(noticeComment)
            .innerJoin(notice).on(noticeComment.notice.id.eq(notice.id))
            .leftJoin(noticeCommentImage).on(noticeCommentImage.noticeComment.id.eq(noticeComment.id))
            .where(
                noticeComment.notice.id.eq(noticeId),
                noticeComment.deletedAt.isNull() // 삭제되지 않은 댓글만 조회
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(noticeComment.count())
            .from(noticeComment)
            .innerJoin(noticeComment.notice, this.notice)
            .where(
                noticeComment.notice.eq(notice),
                noticeComment.deletedAt.isNull() // 삭제되지 않은 댓글만 조회
            );

        return PageableExecutionUtils.getPage(commentList, pageable, countQuery::fetchOne);
    }

    @Override
    public NoticeComment getByIdAndNotice(Long commentId, Notice notice) {
        NoticeComment activeComment = jpaQueryFactory
            .selectFrom(noticeComment)
            .where(
                eqId(commentId),
                eqNotice(notice)
            )
            .fetchOne();

        return Optional.ofNullable(activeComment)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    @Override
    public NoticeComment store(NoticeComment noticeComment) {
        return noticeCommentJpaRepository.save(noticeComment);
    }

    @Override
    public NoticeComment getByIdAndNoticeIdAndMemberId(Long commentId, Long noticeId, Long memberId) {
        NoticeComment writtenComment = jpaQueryFactory
            .selectFrom(noticeComment)
            .innerJoin(notice).on(noticeComment.notice.id.eq(notice.id))
            .innerJoin(member).on(noticeComment.member.id.eq(member.id))
            .where(
                eqId(commentId),
                eqNoticeCommentNotice(noticeComment.notice, noticeId),
                eqNoticeCommentWriter(noticeComment.member, memberId),
                isNotDeleted()
            )
            .fetchOne();

        return Optional.ofNullable(writtenComment)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    @Override
    public boolean isWriter(NoticeComment comment, Member member) {
        return jpaQueryFactory
            .selectOne()
            .from(noticeComment)
            .innerJoin(noticeComment.member, this.member)
            .where(
                noticeComment.eq(comment),
                this.member.eq(member)
            )
            .fetchFirst() != null;
    }

    private BooleanExpression isNotDeleted() {
        return noticeComment.deletedAt.isNull();
    }

    private BooleanExpression eqNoticeCommentWriter(QMember member, Long memberId) {
        return member.id.eq(memberId);
    }

    private BooleanExpression eqNoticeCommentNotice(QNotice notice, Long noticeId) {
        return notice.id.eq(noticeId);
    }

    private BooleanExpression eqId(Long id) {
        return noticeComment.id.eq(id);
    }

    private BooleanExpression eqNotice(Notice notice) {
        return noticeComment.notice.eq(notice);
    }
}
