package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.member.QMemberBlock;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.pinned.PinnedPostComment;
import com.fc8.platform.domain.entity.pinned.QPinnedPost;
import com.fc8.platform.domain.entity.pinned.QPinnedPostComment;
import com.fc8.platform.repository.PinnedPostCommentRepository;
import com.fc8.server.PinnedPostCommentJpaRepository;
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
public class PinnedPostCommentRepositoryImpl implements PinnedPostCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PinnedPostCommentJpaRepository pinnedPostCommentJpaRepository;

    QPinnedPost pinnedPost = QPinnedPost.pinnedPost;
    QPinnedPostComment pinnedPostComment = QPinnedPostComment.pinnedPostComment;
    QMember member = QMember.member;
    QMemberBlock memberBlock = QMemberBlock.memberBlock;

    @Override
    public PinnedPostComment store(PinnedPostComment pinnedPostComment) {
        return pinnedPostCommentJpaRepository.save(pinnedPostComment);
    }

    @Override
    public Page<PinnedPostComment> getAllByPinnedPost(Long memberId, Long pinnedPostId, Pageable pageable) {
        QPinnedPostComment parent = new QPinnedPostComment("parent");
        List<PinnedPostComment> commentList = jpaQueryFactory
                .selectFrom(pinnedPostComment)
                .innerJoin(pinnedPost).on(pinnedPostComment.pinnedPost.eq(pinnedPost))
                .leftJoin(parent).on(pinnedPostComment.parent.eq(parent))
                .where(
                        pinnedPost.id.eq(pinnedPostId),
                        pinnedPost.deletedAt.isNull()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(pinnedPostComment.createdAt.asc())
                .fetch();

        List<Long> blockedOrBlockingMemberIds = getBlockedOrBlockingMemberIds(memberId);

        // 차단된 댓글 여부 설정
        commentList.forEach(comment -> {
            if (blockedOrBlockingMemberIds.contains(comment.getMember().getId())) {
                comment.changeBlockStatus(true);
            }
        });

        JPAQuery<Long> count = jpaQueryFactory
                .select(pinnedPostComment.count())
                .from(pinnedPostComment)
                .innerJoin(pinnedPost).on(pinnedPostComment.pinnedPost.eq(pinnedPost))
                .where(
                        pinnedPost.id.eq(pinnedPostId),
                        pinnedPost.deletedAt.isNull()
                );

        return PageableExecutionUtils.getPage(commentList, pageable, count::fetchOne);
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

    @Override
    public PinnedPostComment getByIdAndPinnedPost(Long id, PinnedPost pinnedPost) {
        PinnedPostComment writtenComment = jpaQueryFactory
                .selectFrom(pinnedPostComment)
                .where(
                        eqId(pinnedPostComment, id),
                        eqPinnedPost(pinnedPostComment, pinnedPost),
                        isNotDeleted(pinnedPostComment)
                )
                .fetchOne();

        return Optional.ofNullable(writtenComment)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    @Override
    public PinnedPostComment getByIdAndPinnedPostIdAndMemberId(Long id, Long pinnedPostId, Long memberId) {
        PinnedPostComment writtenComment = jpaQueryFactory
                .selectFrom(pinnedPostComment)
                .innerJoin(pinnedPost).on(pinnedPostComment.pinnedPost.id.eq(pinnedPost.id))
                .innerJoin(member).on(pinnedPostComment.member.id.eq(member.id))
                .where(
                        eqId(pinnedPostComment, id),
                        eqPinnedPostCommentPost(pinnedPostComment.pinnedPost, pinnedPostId),
                        eqPinnedPostCommentWriter(pinnedPostComment.member, memberId),
                        isNotDeleted(pinnedPost),
                        isNotDeleted(pinnedPostComment)
                )
                .fetchOne();

        return Optional.ofNullable(writtenComment)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    private BooleanExpression eqId(QPinnedPostComment pinnedPostComment, Long id) {
        return pinnedPostComment.id.eq(id);
    }

    private BooleanExpression eqPinnedPost(QPinnedPostComment pinnedPostComment, PinnedPost pinnedPost) {
        return pinnedPostComment.pinnedPost.eq(pinnedPost);
    }

    private BooleanExpression isNotDeleted(QPinnedPost pinnedPost) {
        return pinnedPost.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted(QPinnedPostComment pinnedPostComment) {
        return pinnedPostComment.deletedAt.isNull();
    }

    private BooleanExpression eqPinnedPostCommentPost(QPinnedPost pinnedPost, Long pinnedPostId) {
        return pinnedPost.id.eq(pinnedPostId);
    }

    private BooleanExpression eqPinnedPostCommentWriter(QMember member, Long memberId) {
        return member.id.eq(memberId);
    }

}
