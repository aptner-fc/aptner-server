package com.fc8.server.impl;

import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.qna.QQna;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.server.QnaJpaRepository;
import com.fc8.server.QnaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
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
                isNotDeleted(),
                // 2. 아파트 코드
                eqApartCode(apartCode)
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
                isNotDeleted(),
                // 2. 아파트 코드
                eqApartCode(apartCode)
                // 3. 회원 차단 TODO
            );

        return PageableExecutionUtils.getPage(qnaList, pageable, count::fetchOne);
    }

    private BooleanExpression eqApartCode(String apartCode) {
        return qna.apart.code.eq(apartCode);
    }

    private BooleanExpression isNotDeleted() {
        return qna.deletedAt.isNull();
    }

}
