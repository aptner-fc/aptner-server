package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.terms.QTerms;
import com.fc8.platform.domain.entity.terms.Terms;
import com.fc8.platform.repository.TermsRepository;
import com.fc8.server.TermsJpaRepository;
import com.fc8.server.TermsMemberMappingJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final TermsJpaRepository termsJpaRepository;
    private final TermsMemberMappingJpaRepository termsMemberMappingJpaRepository;

    QTerms terms = QTerms.terms;

    @Override
    public Terms getById(Long id) {
        return termsJpaRepository.findById(id)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TERMS));
    }

    @Override
    public List<Terms> getAllByIds(List<Long> ids) {
        return jpaQueryFactory
                .selectFrom(terms)
                .where(
                        inIds(ids),
                        isUsed(),
                        isNotDeleted()
                )
                .fetch();
    }

    @Override
    public List<Terms> getAllByIsUsed() {
        return jpaQueryFactory
                .selectFrom(terms)
                .where(
                        isUsed(),
                        isNotDeleted()
                )
                .fetch();
    }

    private BooleanExpression isNotDeleted() {
        return terms.deletedAt.isNotNull();
    }

    private BooleanExpression isUsed() {
        return terms.isUsed.isTrue();
    }

    private BooleanExpression inIds(List<Long> ids) {
        return terms.id.in(ids);
    }
}
