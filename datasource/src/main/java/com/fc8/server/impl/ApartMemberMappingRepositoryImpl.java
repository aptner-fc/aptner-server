package com.fc8.server.impl;

import com.fc8.platform.domain.entity.mapping.ApartMemberMapping;
import com.fc8.platform.domain.entity.mapping.QApartMemberMapping;
import com.fc8.platform.repository.ApartMemberMappingRepository;
import com.fc8.server.ApartMemberMappingJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApartMemberMappingRepositoryImpl implements ApartMemberMappingRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ApartMemberMappingJpaRepository apartMemberMappingJpaRepository;

    QApartMemberMapping apartMemberMapping = QApartMemberMapping.apartMemberMapping;

    @Override
    public ApartMemberMapping store(ApartMemberMapping apartMemberMapping) {
        return apartMemberMappingJpaRepository.save(apartMemberMapping);
    }

    @Override
    public List<ApartMemberMapping> storeAll(List<ApartMemberMapping> apartMemberMappings) {
        return apartMemberMappingJpaRepository.saveAll(apartMemberMappings);
    }
}
