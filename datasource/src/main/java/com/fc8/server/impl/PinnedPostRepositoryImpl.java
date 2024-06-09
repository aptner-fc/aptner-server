package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.apartment.QApart;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.pinned.QPinnedPost;
import com.fc8.platform.repository.PinnedPostRepository;
import com.fc8.server.PinnedPostJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PinnedPostRepositoryImpl implements PinnedPostRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PinnedPostJpaRepository pinnedPostJpaRepository;

    QPinnedPost pinnedPost = QPinnedPost.pinnedPost;
    QMember member = QMember.member;
    QApart apart = QApart.apart;
    QCategory category = QCategory.category;

    @Override
    public PinnedPost getPinnedPostByIdAndApartCode(Long memberId, Long pinnedPostId, String apartCode) {
        PinnedPost activePinnedPost = jpaQueryFactory
                .selectFrom(pinnedPost)
                .where(
                        eqId(pinnedPost, pinnedPostId),
                        eqApartCode(pinnedPost, apartCode)
                )
                .fetchOne();

        return Optional.ofNullable(activePinnedPost)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public PinnedPost getByIdAndApartCode(Long pinnedPostId, String apartCode) {
        PinnedPost activePost = jpaQueryFactory
                .selectFrom(pinnedPost)
                .where(
                        eqId(pinnedPost, pinnedPostId),
                        eqApartCode(pinnedPost, apartCode),
                        isNotDeleted(pinnedPost)
                )
                .fetchOne();

        return Optional.ofNullable(activePost)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public List<PinnedPost> getAllByApartCodeAndCategoryCode(String apartCode, String categoryCode) {
        return jpaQueryFactory
                .selectFrom(pinnedPost)
                .innerJoin(category).on(pinnedPost.category.eq(category))
                .innerJoin(apart).on(pinnedPost.apart.eq(apart))
                .where(
                        category.code.eq(categoryCode),
                        category.isUsed,
                        category.parent.isNull()
                )
                .fetch();
    }

    private BooleanExpression isNotDeleted(QPinnedPost pinnedPost) {
        return pinnedPost.deletedAt.isNull();
    }

    private BooleanExpression eqId(QPinnedPost pinnedPost, Long postId) {
        return pinnedPost.id.eq(postId);
    }

    private BooleanExpression eqApartCode(QPinnedPost pinnedPost, String apartCode) {
        return pinnedPost.apart.code.eq(apartCode);
    }
}
