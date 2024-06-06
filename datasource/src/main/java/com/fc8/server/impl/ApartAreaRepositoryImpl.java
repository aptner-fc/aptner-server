package com.fc8.server.impl;

import com.fc8.platform.domain.entity.apartment.ApartArea;
import com.fc8.platform.domain.entity.apartment.QApart;
import com.fc8.platform.domain.entity.apartment.QApartArea;
import com.fc8.platform.repository.ApartAreaRepository;
import com.fc8.server.ApartAreaJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApartAreaRepositoryImpl implements ApartAreaRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ApartAreaJpaRepository apartAreaJpaRepository;

    QApart apart = QApart.apart;
    QApartArea apartArea = QApartArea.apartArea;

    @Override
    public List<ApartArea> getAllByApartCode(String apartCode) {
        return jpaQueryFactory
                .selectFrom(apartArea)
                .innerJoin(apart).on(apartArea.apart.id.eq(apart.id))
                .where(
                        apartArea.apart.code.eq(apartCode),
                        apartArea.isUsed.isTrue()
                )
                .fetch();
    }

    @Override
    public ApartArea getById(Long id) {
        return jpaQueryFactory
                .selectFrom(apartArea)
                .where(
                        apartArea.isUsed.isTrue(),
                        apartArea.id.eq(id)
                )
                .fetchOne();
    }
}
