package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.apartment.QApart;
import com.fc8.platform.domain.entity.apartment.QApartArea;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.mapping.QApartAreaPostMapping;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.member.QMemberBlock;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.QPost;
import com.fc8.platform.domain.entity.post.QPostComment;
import com.fc8.platform.domain.entity.post.QPostFile;
import com.fc8.platform.domain.enums.SearchType;
import com.fc8.platform.dto.record.CategoryInfo;
import com.fc8.platform.dto.record.PostSummary;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.PostRepository;
import com.fc8.server.PostJpaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostJpaRepository postJpaRepository;

    QPost post = QPost.post;
    QApartArea apartArea = QApartArea.apartArea;
    QApartAreaPostMapping apartAreaPostMapping = QApartAreaPostMapping.apartAreaPostMapping;
    QMember member = QMember.member;
    QMemberBlock memberBlock = QMemberBlock.memberBlock;
    QCategory category = QCategory.category;
    QApart apart = QApart.apart;
    QPostComment postComment = QPostComment.postComment;
    QPostFile postFile = QPostFile.postFile;

    @Override
    public Post store(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Page<PostSummary> getPostSummaryList(Long memberId, String apartCode, Long apartAreaId, Pageable pageable, String search, SearchType type, String categoryCode) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        List<PostSummary> postSummaryList = jpaQueryFactory
            .select(Projections.constructor(
                PostSummary.class,
                post.id,
                post.title,
                post.thumbnailPath,
                post.createdAt,
                post.updatedAt,
                Projections.constructor(
                    WriterInfo.class,
                    post.member.id,
                    post.member.name,
                    post.member.nickname
                ),
                Projections.constructor(
                    CategoryInfo.class,
                    post.category.id,
                    post.category.type,
                    post.category.code,
                    post.category.name
                ),
                jpaQueryFactory.select(postComment.count())
                    .from(postComment)
                    .where(postComment.post.id.eq(post.id)),
                post.viewCount,
                jpaQueryFactory.select(postFile.count())
                    .from(postFile)
                    .where(postFile.post.id.eq(post.id)).gt(0L)
            ))
            .from(post)
            .innerJoin(category).on(post.category.id.eq(category.id))
            .innerJoin(member).on(post.member.id.eq(member.id))
            .leftJoin(apartAreaPostMapping).on(post.id.eq(apartAreaPostMapping.post.id))
            .leftJoin(apartArea).on(apartAreaPostMapping.apartArea.id.eq(apartArea.id))
            .where(
                // 1. 삭제된 포스트
                isNotDeleted(post),
                // 2. 아파트 코드
                eqApartCode(post, apartCode),
                // 3. 차단한 회원 및 차단된 회원 포스트 제거
                removeMemberBlock(post.member, blockedMemberIds, blockingMemberIds),
                // 4. 카테고리
                eqCategoryCode(category, categoryCode),
                // 5. 검색어
                containsSearch(post, member, search, type),
                // 6. 평수 조회
                containsApartArea(apartAreaPostMapping.apartArea, apartAreaId)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(post.createdAt.desc())
            .fetch();

        JPAQuery<Long> count = jpaQueryFactory
            .select(post.count())
            .from(post)
            .innerJoin(category).on(post.category.id.eq(category.id))
            .innerJoin(member).on(post.member.id.eq(member.id))
            .leftJoin(apartAreaPostMapping).on(post.id.eq(apartAreaPostMapping.post.id))
            .leftJoin(apartArea).on(apartAreaPostMapping.apartArea.id.eq(apartArea.id))
            .where(
                // 1. 삭제된 포스트
                isNotDeleted(post),
                // 2. 아파트 코드
                eqApartCode(post, apartCode),
                // 3. 차단한 회원 및 차단된 회원 포스트 제거
                removeMemberBlock(post.member, blockedMemberIds, blockingMemberIds),
                // 4. 카테고리
                eqCategoryCode(category, categoryCode),
                // 5. 검색어
                containsSearch(post, member, search, type),
                // 6. 평수 조회
                containsApartArea(apartAreaPostMapping.apartArea, apartAreaId)
            );

        return PageableExecutionUtils.getPage(postSummaryList, pageable, count::fetchOne);
    }

    @Override
    public void updateViewCount(Long postId, Long viewCount) {
        jpaQueryFactory
                .update(post)
                .set(post.viewCount, viewCount)
                .where(post.id.eq(postId))
                .execute();
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
    public Post getPostWithCategoryByIdAndApartCode(Long memberId, Long postId, String apartCode) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        Post activePost = jpaQueryFactory
                .selectFrom(post)
                .where(
                        eqId(post, postId),
                        eqApartCode(post, apartCode),
                        removeMemberBlock(post.member, blockedMemberIds, blockingMemberIds)
                )
                .fetchOne();

        return Optional.ofNullable(activePost)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public boolean isWriter(Post activePost, Member activeMember) {
        return jpaQueryFactory
                .selectOne()
                .from(post)
                .innerJoin(member).on(post.member.eq(member))
                .where(
                        post.eq(activePost),
                        member.eq(activeMember)
                )
                .fetchFirst() != null;
    }

    @Override
    public List<Post> getAllByIdsAndMember(List<Long> postIds, Member activeMember) {
        return jpaQueryFactory
                .selectFrom(post)
                .innerJoin(member).on(post.member.id.eq(member.id))
                .where(
                        eqPostWriter(post.member, activeMember.getId()),
                        post.id.in(postIds)
                )
                .fetch();
    }

    @Override
    public List<Post> getPostListByKeyword(Long memberId, String apartCode, String keyword, int pinnedPostCount) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        return jpaQueryFactory
            .selectFrom(post)
            .innerJoin(member).on(post.member.id.eq(member.id))
            .innerJoin(apart).on(post.apart.eq(apart))
            .where(
                // 아파트 체크
                apart.code.eq(apartCode),

                // 삭제 여부
                isNotDeleted(post),

                // 차단한 회원 및 차단된 회원 포스트 제거
                removeMemberBlock(post.member, blockedMemberIds, blockingMemberIds),

                // 검색어
                containsSearch(post, member, keyword, SearchType.TITLE_AND_CONTENT)
            )
            .limit(5 - pinnedPostCount)
            .orderBy(post.createdAt.desc())
            .fetch();
    }

    @Override
    public Long getPostCountByKeyword(Long memberId, String apartCode, String keyword) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        Long count = jpaQueryFactory
            .select(post.count())
            .from(post)
            .innerJoin(member).on(post.member.id.eq(member.id))
            .innerJoin(apart).on(post.apart.eq(apart))
            .where(
                // 아파트 체크
                apart.code.eq(apartCode),

                // 삭제 여부
                isNotDeleted(post),

                // 차단한 회원 및 차단된 회원 포스트 제거
                removeMemberBlock(post.member, blockedMemberIds, blockingMemberIds),

                // 검색어
                containsSearch(post, member, keyword, SearchType.TITLE_AND_CONTENT)
            )
            .fetchOne();

        return count != null ? count : 0;
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

    private BooleanExpression removeMemberBlock(QMember member, List<Long> blockedMemberIds, List<Long> blockingMemberIds) {
        return member.id.notIn(blockedMemberIds).and(member.id.notIn(blockingMemberIds));
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

    private BooleanExpression containsSearch(QPost post, QMember member, String search, SearchType type) {
        if (!StringUtils.hasText(search)) {
            return null;
        }

        return switch (type) {
            case TITLE -> post.title.contains(search);
            case CONTENT -> post.content.contains(search);
            case TITLE_AND_CONTENT -> post.title.contains(search).or(post.content.contains(search));
            case WRITER -> member.name.contains(search).or(member.nickname.contains(search));
        };
    }

    private BooleanExpression containsApartArea(QApartArea apartArea, Long apartAreaId) {
        return Optional.ofNullable(apartAreaId)
                .map(apartArea.id::eq)
                .orElse(null);
    }

    private BooleanExpression eqCategoryCode(QCategory category, String categoryCode) {
        if (!StringUtils.hasText(categoryCode)) {
            return null;
        }

        return category.code.eq(categoryCode);
    }

}
