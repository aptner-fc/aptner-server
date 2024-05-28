package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.QPost;
import com.fc8.platform.repository.PostRepository;
import com.fc8.server.PostJpaRepository;
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
public class PostRepositoryImpl implements PostRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostJpaRepository postJpaRepository;

    QPost post = QPost.post;
    QMember member = QMember.member;
    QCategory category = QCategory.category;

    @Override
    public Post store(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Page<Post> getPostListByApartCode(Long memberId, String apartCode, Pageable pageable, String search) {
        List<Post> postList = jpaQueryFactory
                .selectFrom(post)
                .innerJoin(category).on(post.category.id.eq(category.id))
                .where(
                        // 1. 삭제된 포스트
                        isNotDeleted(post),
                        // 2. 아파트 코드
                        eqApartCode(post, apartCode)
                        // 3. 회원 차단 TODO

                        // 4. 검색어
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = jpaQueryFactory
                .select(post.count())
                .from(post)
                .innerJoin(category).on(post.category.id.eq(category.id))
                .where(
                        // 1. 삭제된 포스트
                        isNotDeleted(post),
                        // 2. 아파트 코드
                        eqApartCode(post, apartCode)
                        // 3. 회원 차단 TODO
                );

        return PageableExecutionUtils.getPage(postList, pageable, count::fetchOne);
    }

    @Override
    public Post getByIdAndApartCode(Long postId, String apartCode) {
        Post activePost = jpaQueryFactory
                .selectFrom(post)
                .where(
                        eqId(post, postId),
                        eqApartCode(post, apartCode),
                        isNotDeleted(post)
                )
                .fetchOne();

        return Optional.ofNullable(activePost)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public Post getByIdAndMemberId(Long postId, Long memberId) {
        Post activePost = jpaQueryFactory
                .selectFrom(post)
                .innerJoin(member).on(post.member.id.eq(member.id))
                .where(
                        eqId(post, postId),
                        eqPostWriter(post.member, memberId),
                        isNotDeleted(post)
                )
                .fetchOne();

        return Optional.ofNullable(activePost)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    private BooleanExpression eqPostWriter(QMember member, Long memberId) {
        return member.id.eq(memberId);
    }

    @Override
    public Post getPostWithCategoryByIdAndApartCode(Long postId, String apartCode) {
        Post activePost = jpaQueryFactory
                .selectFrom(post)
                .innerJoin(category).on(post.category.id.eq(category.id))
                .where(
                        eqId(post, postId),
                        eqApartCode(post, apartCode)
                )
                .fetchOne();

        return Optional.ofNullable(activePost)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public boolean isWriter(Post activePost, Member loginMember) {
        return jpaQueryFactory
                .selectOne()
                .from(post)
                .innerJoin(member).on(post.member.eq(member))
                .where(
                        post.eq(activePost),
                        member.eq(loginMember)
                )
                .fetchFirst() != null;
    }

    private BooleanExpression eqId(QPost post, Long postId) {
        return post.id.eq(postId);
    }

    private BooleanExpression eqApartCode(QPost post, String apartCode) {
        return post.apart.code.eq(apartCode);
    }

    private BooleanExpression isNotDeleted(QPost post) {
        return post.deletedAt.isNull();
    }

}
