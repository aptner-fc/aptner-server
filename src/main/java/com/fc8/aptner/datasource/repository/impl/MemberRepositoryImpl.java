package com.fc8.aptner.datasource.repository.impl;

import com.fc8.aptner.common.exception.InvalidParamException;
import com.fc8.aptner.common.exception.code.ErrorCode;
import com.fc8.aptner.core.domain.entity.member.Member;
import com.fc8.aptner.core.domain.entity.member.QMember;
import com.fc8.aptner.core.repository.MemberRepository;
import com.fc8.aptner.datasource.repository.MemberJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberJpaRepository memberJpaRepository;

    QMember member = QMember.member;

    @Override
    public Member getMemberByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Override
    public boolean existActiveEmail(String email) {
        return jpaQueryFactory
                .selectOne()
                .from(member)
                .where(
                        eqEmail(email),
                        isActive()
                )
                .fetchFirst() != null;
    }

    @Override
    public boolean existNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }

    @Override
    public Member store(Member member) {
        return memberJpaRepository.save(member);
    }

    private BooleanExpression isActive() {
        return member.deletedAt.isNull();
    }

    private BooleanExpression eqEmail(String email) {
        return member.email.eq(email);
    }

}
