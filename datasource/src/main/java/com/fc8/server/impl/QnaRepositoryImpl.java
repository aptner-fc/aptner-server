package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.qna.QQna;
import com.fc8.platform.domain.entity.qna.Qna;
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

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QnaRepositoryImpl implements QnaRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QnaJpaRepository qnaJpaRepository;

    QQna qna = QQna.qna;
    QMember member = QMember.member;
    QCategory category = QCategory.category;

    @Override
    public Qna store(Qna qna) {
        return qnaJpaRepository.save(qna);
    }

    @Override
    public Page<Qna> getQnaListByApartCode(Long memberId, String apartCode, Pageable pageable, String search) {
        List<Qna> qnaList = jpaQueryFactory
            .selectFrom(qna)
            .innerJoin(category).on(qna.category.id.eq(category.id))
            .where(
                // 1. 삭제된 게시글
                isNotDeleted(qna),
                // 2. 아파트 코드
                eqApartCode(qna, apartCode)
                // 3. 회원 차단 TODO

                // 4. 검색어
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> count = jpaQueryFactory
            .select(qna.count())
            .from(qna)
            .innerJoin(category).on(qna.category.id.eq(category.id))
            .where(
                // 1. 삭제된 게시글
                isNotDeleted(qna),
                // 2. 아파트 코드
                eqApartCode(qna, apartCode)
                // 3. 회원 차단 TODO
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
    public Qna getQnaWithCategoryByIdAndApartCode(Long qnaId, String apartCode) {
        Qna activeQna = jpaQueryFactory
            .selectFrom(qna)
            .innerJoin(category).on(qna.category.id.eq(category.id))
            .where(
                eqId(qna, qnaId),
                eqApartCode(qna, apartCode)
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

}
