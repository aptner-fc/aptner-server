package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.member.QMemberBlock;
import com.fc8.platform.domain.entity.qna.QQna;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.enums.SearchType;
import com.fc8.platform.repository.QnaRepository;
import com.fc8.server.QnaJpaRepository;
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

    @Override
    public Qna store(Qna qna) {
        return qnaJpaRepository.save(qna);
    }

    @Override
    public Page<Qna> getQnaListByApartCode(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = jpaQueryFactory
                .select(memberBlock.blocked.id)
                .from(memberBlock)
                .where(memberBlock.member.id.eq(memberId))
                .fetch();

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = jpaQueryFactory
                .select(memberBlock.member.id)
                .from(memberBlock)
                .where(memberBlock.blocked.id.eq(memberId))
                .fetch();

        List<Qna> qnaList = jpaQueryFactory
                .selectFrom(qna)
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

        JPAQuery<Long> count = jpaQueryFactory
                .select(qna.count())
                .from(qna)
                .innerJoin(category).on(qna.category.id.eq(category.id))
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
                );

        return PageableExecutionUtils.getPage(qnaList, pageable, count::fetchOne);
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
    public Qna getQnaWithCategoryByIdAndApartCode(Long memberId, Long qnaId, String apartCode) {
        // 해당 회원이 차단한 회원의 목록
        List<Long> blockedMemberIds = jpaQueryFactory
                .select(memberBlock.blocked.id)
                .from(memberBlock)
                .where(memberBlock.member.id.eq(memberId))
                .fetch();

        // 해당 회원이 차단당한 회원의 목록
        List<Long> blockingMemberIds = jpaQueryFactory
                .select(memberBlock.member.id)
                .from(memberBlock)
                .where(memberBlock.blocked.id.eq(memberId))
                .fetch();

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
