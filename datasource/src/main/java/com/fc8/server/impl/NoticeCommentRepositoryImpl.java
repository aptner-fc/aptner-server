package com.fc8.server.impl;

import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.notice.QNotice;
import com.fc8.platform.domain.entity.notice.QNoticeComment;
import com.fc8.platform.domain.entity.notice.QNoticeCommentImage;
import com.fc8.platform.dto.record.NoticeCommentInfo;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.NoticeCommentRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeCommentRepositoryImpl implements NoticeCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

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
}
