package com.fc8.aptner.datasource.repository.impl;

import com.fc8.aptner.core.domain.entity.history.QTermsMemberMapping;
import com.fc8.aptner.core.domain.entity.history.TermsMemberMapping;
import com.fc8.aptner.core.repository.TermsMemberMappingRepository;
import com.fc8.aptner.datasource.repository.TermsMemberMappingJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TermsMemberMappingRepositoryImpl implements TermsMemberMappingRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final TermsMemberMappingJpaRepository termsMemberMappingJpaRepository;

    QTermsMemberMapping termsMemberMapping = QTermsMemberMapping.termsMemberMapping;

    @Override
    public List<TermsMemberMapping> storeAll(List<TermsMemberMapping> termsMemberMappings) {
        return termsMemberMappingJpaRepository.saveAll(termsMemberMappings);
    }
}
