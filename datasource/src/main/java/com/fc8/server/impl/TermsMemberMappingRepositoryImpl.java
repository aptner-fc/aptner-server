package com.fc8.server.impl;

import com.fc8.platform.domain.entity.mapping.QTermsMemberMapping;
import com.fc8.platform.domain.entity.mapping.TermsMemberMapping;
import com.fc8.platform.repository.TermsMemberMappingRepository;
import com.fc8.server.TermsMemberMappingJpaRepository;
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
