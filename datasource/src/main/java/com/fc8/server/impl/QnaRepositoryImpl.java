package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.apartment.QApart;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.member.QMemberBlock;
import com.fc8.platform.domain.entity.qna.QQna;
import com.fc8.platform.domain.entity.qna.QQnaComment;
import com.fc8.platform.domain.entity.qna.QQnaFile;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.enums.SearchType;
import com.fc8.platform.dto.record.CategoryInfo;
import com.fc8.platform.dto.record.QnaInfo;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.QnaRepository;
import com.fc8.server.QnaJpaRepository;
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
public class QnaRepositoryImpl implements QnaRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QnaJpaRepository qnaJpaRepository;

    QQna qna = QQna.qna;
    QMember member = QMember.member;
    QMemberBlock memberBlock = QMemberBlock.memberBlock;
    QCategory category = QCategory.category;
    QApart apart = QApart.apart;
    QQnaComment qnaComment = QQnaComment.qnaComment;
    QQnaFile qnaFile = QQnaFile.qnaFile;

    @Override
    public Qna store(Qna qna) {
        return qnaJpaRepository.save(qna);
    }

    @Override
    public Page<QnaInfo> getQnaInfoList(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        List<QnaInfo> qnaInfoList = jpaQueryFactory
                .select(Projections.constructor(
                        QnaInfo.class,
                        qna.id,
                        qna.title,
                        qna.createdAt,
                        qna.updatedAt,
                        Projections.constructor(
                                WriterInfo.class,
                                qna.member.id,
                                qna.member.name,
                                qna.member.nickname
                        ),
                        Projections.constructor(
                                CategoryInfo.class,
                                qna.category.id,
                                qna.category.type,
                                qna.category.code,
                                qna.category.name
                        ),
                        qna.isPrivate,
                        jpaQueryFactory.select(qnaComment.count())
                                .from(qnaComment)
                                .where(qnaComment.qna.id.eq(qna.id)),
                        qna.viewCount,
                        jpaQueryFactory.select(qnaFile.count())
                                .from(qnaFile)
                                .where(qnaFile.qna.id.eq(qna.id)).gt(0L)
                ))
                .from(qna)
                .innerJoin(category).on(qna.category.id.eq(category.id))
                .innerJoin(member).on(qna.member.id.eq(member.id))
                .where(
                        // 1. 삭제된 게시글
                        isNotDeleted(qna),
                        // 2. 아파트 코드
                        eqApartCode(qna, apartCode),
                        // 3. 차단한 회원 및 차단된 회원 포스트 제거
                        removeMemberBlock(qna.member, blockedMemberIds, blockingMemberIds),
                        // 4. 카테고리
                        eqCategoryCode(category, categoryCode),
                        // 5. 검색어
                        containsSearch(qna, member, search, type)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qna.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(qna.count())
            .from(qna)
            .innerJoin(qna.category, category)
            .where(
                isNotDeleted(qna),
                eqApartCode(qna, apartCode),
                removeMemberBlock(qna.member, blockedMemberIds, blockingMemberIds),
                eqCategoryCode(category, categoryCode),
                containsSearch(qna, member, search, type)
            );

        return PageableExecutionUtils.getPage(qnaInfoList, pageable, countQuery::fetchOne);
    }

    @Override
    public void updateViewCount(Long qnaId, Long viewCount) {
        jpaQueryFactory
                .update(qna)
                .set(qna.viewCount, viewCount)
                .where(qna.id.eq(qnaId))
                .execute();
    }

    @Override
    public Qna getByIdAndApartCode(Long qnaId, String apartCode) {
        Qna activeQna = jpaQueryFactory
            .selectFrom(qna)
            .where(
                eqId(qna, qnaId),
                eqApartCode(qna, apartCode),
                isNotDeleted(qna)
            )
            .fetchOne();

        return Optional.ofNullable(activeQna)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public Qna getByIdAndMemberId(Long postId, Long memberId) {
        Qna activeQna = jpaQueryFactory
            .selectFrom(qna)
            .innerJoin(member).on(qna.member.id.eq(member.id))
            .where(
                eqId(qna, postId),
                eqQnaWriter(qna.member, memberId),
                isNotDeleted(qna)
            )
            .fetchOne();

        return Optional.ofNullable(activeQna)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public List<Qna> getAllByIdsAndMember(List<Long> postIds, Member activeMember) {
        return jpaQueryFactory
                .selectFrom(qna)
                .innerJoin(member).on(qna.member.id.eq(member.id))
                .where(
                        eqQnaWriter(qna.member, activeMember.getId()),
                        qna.id.in(postIds)
                )
                .fetch();
    }

    @Override
    public Qna getById(Long qnaId) {
        return qnaJpaRepository.findById(qnaId)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public List<Qna> getQnaListByKeyword(Long memberId, String apartCode, String keyword, int pinnedQnaCount) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        return jpaQueryFactory
            .selectFrom(qna)
            .innerJoin(member).on(qna.member.id.eq(member.id))
            .innerJoin(apart).on(qna.apart.eq(apart))
            .where(
                // 아파트 체크
                apart.code.eq(apartCode),

                // 삭제 여부
                isNotDeleted(qna),

                // 차단한 회원 및 차단된 회원 포스트 제거
                removeMemberBlock(qna.member, blockedMemberIds, blockingMemberIds),

                // 검색어
                containsSearch(qna, member, keyword, SearchType.TITLE_AND_CONTENT)
            )
            .limit(5 - pinnedQnaCount)
            .orderBy(qna.createdAt.desc())
            .fetch();
    }

    @Override
    public Long getQnaCountByKeyword(Long memberId, String apartCode, String keyword) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        Long count = jpaQueryFactory
            .select(qna.count())
            .from(qna)
            .innerJoin(member).on(qna.member.id.eq(member.id))
            .innerJoin(apart).on(qna.apart.eq(apart))
            .where(
                // 아파트 체크
                apart.code.eq(apartCode),

                // 삭제 여부
                isNotDeleted(qna),

                // 차단한 회원 및 차단된 회원 포스트 제거
                removeMemberBlock(qna.member, blockedMemberIds, blockingMemberIds),

                // 검색어
                containsSearch(qna, member, keyword, SearchType.TITLE_AND_CONTENT)
            )
            .fetchOne();

        return count != null ? count : 0;
    }

    @Override
    public Qna getQnaWithCategoryByIdAndApartCode(Long memberId, Long qnaId, String apartCode) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        Qna activeQna = jpaQueryFactory
            .selectFrom(qna)
            .where(
                eqId(qna, qnaId),
                eqApartCode(qna, apartCode),
                    removeMemberBlock(qna.member, blockedMemberIds, blockingMemberIds)
            )
            .fetchOne();

        return Optional.ofNullable(activeQna)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public boolean isWriter(Qna activeQna, Member loginMember) {
        return jpaQueryFactory
            .selectOne()
            .from(qna)
            .innerJoin(member).on(qna.member.eq(member))
            .where(
                qna.eq(activeQna),
                member.eq(loginMember)
            )
            .fetchFirst() != null;
    }

    private BooleanExpression removeMemberBlock(QMember member, List<Long> blockedMemberIds, List<Long> blockingMemberIds) {
        return member.id.notIn(blockedMemberIds).and(member.id.notIn(blockingMemberIds));
    }

    private List<Long> getBlockingMemberIds(Long memberId) {
        return jpaQueryFactory
            .select(memberBlock.member.id)
            .from(memberBlock)
            .where(memberBlock.blocked.id.eq(memberId))
            .fetch();
    }

    private List<Long> getBlockedMemberIds(Long memberId) {
        return jpaQueryFactory
            .select(memberBlock.blocked.id)
            .from(memberBlock)
            .where(memberBlock.member.id.eq(memberId))
            .fetch();
    }

    private BooleanExpression eqId(QQna qna, Long qnaId) {
        return qna.id.eq(qnaId);
    }

    private BooleanExpression eqQnaWriter(QMember member, Long memberId) {
        return member.id.eq(memberId);
    }

    private BooleanExpression eqApartCode(QQna qna, String apartCode) {
        return qna.apart.code.eq(apartCode);
    }

    private BooleanExpression isNotDeleted(QQna qna) {
        return qna.deletedAt.isNull();
    }

    private BooleanExpression containsSearch(QQna qna, QMember member, String search, SearchType type) {
        if (!StringUtils.hasText(search)) {
            return null;
        }

        return switch (type) {
            case TITLE -> qna.title.contains(search);
            case CONTENT -> qna.content.contains(search);
            case TITLE_AND_CONTENT -> qna.title.contains(search).or(qna.content.contains(search));
            case WRITER -> member.name.contains(search).or(member.nickname.contains(search));
        };
    }

    private BooleanExpression eqCategoryCode(QCategory category, String categoryCode) {
        if (!StringUtils.hasText(categoryCode)) {
            return null;
        }

        return category.code.eq(categoryCode);
    }

}
