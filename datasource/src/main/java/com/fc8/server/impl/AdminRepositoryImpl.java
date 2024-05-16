package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.admin.QAdmin;
import com.fc8.platform.domain.entity.apartment.QApart;
import com.fc8.platform.domain.enums.ApartType;
import com.fc8.platform.repository.AdminRepository;
import com.fc8.server.AdminJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final AdminJpaRepository adminJpaRepository;

    QAdmin aptAdmin = QAdmin.admin;
    QApart apart = QApart.apart;

    @Override
    public Admin getMemberByEmail(String email) {
        return adminJpaRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_ADMIN));

    }

    @Override
    public ApartType getApartTypeByAdmin(Admin admin) {
        return jpaQueryFactory
                .select(
                        apart.type
                )
                .from(aptAdmin)
                .innerJoin(apart).on(aptAdmin.apart.id.eq(apart.id))
                .where(
                        aptAdmin.eq(admin),
                        apart.isUsed.isTrue()
                )
                .fetchFirst();
    }

    @Override
    public boolean existActiveEmail(String email) {
        return jpaQueryFactory
                .selectOne()
                .from(aptAdmin)
                .where(
                        eqEmail(email),
                        isActive()
                )
                .fetchFirst() != null;
    }

    @Override
    public boolean existNickname(String nickname) {
        return adminJpaRepository.existsByNickname(nickname);
    }

    @Override
    public Admin store(Admin admin) {
        return adminJpaRepository.save(admin);
    }

    private BooleanExpression isActive() {
        return aptAdmin.deletedAt.isNull();
    }

    private BooleanExpression eqEmail(String email) {
        return aptAdmin.email.eq(email);
    }

}
