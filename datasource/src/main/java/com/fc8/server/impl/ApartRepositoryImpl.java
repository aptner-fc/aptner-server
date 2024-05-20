package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.entity.apartment.QApart;
import com.fc8.platform.domain.entity.mapping.QApartMemberMapping;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.repository.ApartRepository;
import com.fc8.server.ApartJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApartRepositoryImpl implements ApartRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ApartJpaRepository apartJpaRepository;

    QApart apart = QApart.apart;
    QApartMemberMapping apartMemberMapping = QApartMemberMapping.apartMemberMapping;

    @Override
    public Apart getByCode(String code) {
        return apartJpaRepository.findByCodeAndIsUsedTrue(code)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_USED_APART));
    }

    @Override
    public Apart getMainApartByMember(Member member) {
        Apart mainApart = jpaQueryFactory
                .select(this.apart)
                .from(apartMemberMapping)
                .innerJoin(this.apart).on(apartMemberMapping.apart.eq(this.apart))
                .where(
                        isUsed(this.apart),
                        isMain(apartMemberMapping),
                        eqApartMember(apartMemberMapping, member)
                )
                .fetchOne();

        return Optional.ofNullable(mainApart)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER_APART));
    }

    @Override
    public List<Apart> getNotMainApartListByMember(Member member) {
        return jpaQueryFactory
                .select(apart)
                .from(apartMemberMapping)
                .innerJoin(apart).on(apartMemberMapping.apart.eq(apart))
                .where(
                        isUsed(apart),
                        isNotMain(apartMemberMapping),
                        eqApartMember(apartMemberMapping, member)
                )
                .fetch();
    }

    @Override
    public String getContactByCode(String code) {
        return jpaQueryFactory
                .select(apart.contact)
                .from(apart)
                .where(
                        eqCode(apart, code),
                        isUsed(apart)
                )
                .fetchOne();
    }

    private BooleanExpression eqApartMember(QApartMemberMapping apartMemberMapping, Member member) {
        return apartMemberMapping.member.eq(member);
    }

    private BooleanExpression eqCode(QApart apart, String code) {
        return apart.code.eq(code);
    }

    private BooleanExpression isUsed(QApart apart) {
        return apart.isUsed.isTrue();
    }

    private BooleanExpression isMain(QApartMemberMapping apartMemberMapping) {
        return apartMemberMapping.isMain.isTrue();
    }

    private BooleanExpression isNotMain(QApartMemberMapping apartMemberMapping) {
        return apartMemberMapping.isMain.isFalse();
    }
}
