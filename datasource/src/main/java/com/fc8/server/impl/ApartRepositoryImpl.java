package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.repository.ApartRepository;
import com.fc8.server.ApartJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApartRepositoryImpl implements ApartRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ApartJpaRepository apartJpaRepository;


    @Override
    public Apart getByCode(String code) {
        return apartJpaRepository.findByCodeAndIsUsedTrue(code)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_USED_APART));
    }

}
