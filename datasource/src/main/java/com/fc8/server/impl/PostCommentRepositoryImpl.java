package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.post.*;
import com.fc8.platform.dto.record.PostCommentInfo;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.PostCommentRepository;
import com.fc8.server.PostCommentJpaRepository;
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
public class PostCommentRepositoryImpl implements PostCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostCommentJpaRepository postCommentJpaRepository;

    QPost post = QPost.post;
    QPostComment postComment = QPostComment.postComment;
    QMember member = QMember.member;
    QPostCommentImage postCommentImage = QPostCommentImage.postCommentImage;

    @Override
    public PostComment getByIdAndPost(Long id, Post post) {
        PostComment writtenComment = jpaQueryFactory
                .selectFrom(postComment)
                .where(
                        eqId(id),
                        eqPost(post),
                        isNotDeleted(postComment)
                )
                .fetchOne();

        return Optional.ofNullable(writtenComment)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    @Override
    public PostComment store(PostComment postComment) {
        return postCommentJpaRepository.save(postComment);
    }

    @Override
    public PostComment getByIdAndPostIdAndMemberId(Long id, Long postId, Long memberId) {
        PostComment writtenComment = jpaQueryFactory
                .selectFrom(postComment)
                .innerJoin(post).on(postComment.post.id.eq(post.id))
                .innerJoin(member).on(postComment.member.id.eq(member.id))
                .where(
                        eqId(id),
                        eqPostCommentPost(postComment.post, postId),
                        eqPostCommentWriter(postComment.member, memberId),
                        isNotDeleted(post),
                        isNotDeleted(postComment)
                )
                .fetchOne();

        return Optional.ofNullable(writtenComment)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    @Override
    public boolean isWriter(PostComment activeComment, Member loginmember) {
        return jpaQueryFactory
            .selectOne()
            .from(postComment)
            .innerJoin(postComment.member, member)
            .where(
                postComment.eq(activeComment),
                member.eq(loginmember)
            )
            .fetchFirst() != null;
    }

    @Override
    public Page<PostCommentInfo> getCommentListByPost(Long postId, Pageable pageable) {
        List<PostCommentInfo> commentList = jpaQueryFactory
            .select(Projections.constructor(
                PostCommentInfo.class,
                postComment.id,
                postComment.parent.id,
                postComment.content,
                postComment.createdAt,
                postComment.updatedAt,
                postCommentImage.imagePath.as("imageUrl"),
                Projections.constructor(
                    WriterInfo.class,
                    postComment.member.id,
                    postComment.member.name,
                    postComment.member.nickname
                )
            ))
            .from(postComment)
            .innerJoin(post).on(postComment.post.id.eq(post.id))
            .leftJoin(postCommentImage).on(postCommentImage.postComment.id.eq(postComment.id))
            .where(
                postComment.post.id.eq(postId),
                postComment.deletedAt.isNull()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(postComment.count())
            .from(postComment)
            .innerJoin(postComment.post, this.post)
            .where(
                postComment.post.eq(post),
                postComment.deletedAt.isNull() // 삭제되지 않은 댓글만 조회
            );

        return PageableExecutionUtils.getPage(commentList, pageable, countQuery::fetchOne);
    }

    @Override
    public List<PostComment> getAllByIdsAndMember(List<Long> postCommentIds, Member activeMember) {
        return jpaQueryFactory
                .selectFrom(postComment)
                .innerJoin(post).on(postComment.post.id.eq(post.id))
                .innerJoin(member).on(postComment.member.id.eq(member.id))
                .where(
                        isNotDeleted(post),
                        isNotDeleted(postComment),
                        eqPostCommentWriter(postComment.member, activeMember.getId()),
                        postComment.id.in(postCommentIds)
                )
                .fetch();
    }

    private BooleanExpression eqPostCommentWriter(QMember member, Long memberId) {
        return member.id.eq(memberId);
    }

    private BooleanExpression eqPostCommentPost(QPost post, Long postId) {
        return post.id.eq(postId);
    }

    private BooleanExpression isNotDeleted(QPost post) {
        return post.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted(QPostComment postComment) {
        return postComment.deletedAt.isNull();
    }

    private BooleanExpression eqPost(Post post) {
        return postComment.post.eq(post);
    }

    private BooleanExpression eqId(Long id) {
        return postComment.id.eq(id);
    }

}
