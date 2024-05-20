package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.repository.MemberRepository;
import com.fc8.server.MemberJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberJpaRepository memberJpaRepository;

    QMember member = QMember.member;

    @Override
    public Member getByEmail(String email) {
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

    @Override
    public Member getActiveMemberById(Long id) {
        Member activeMember = jpaQueryFactory
                .selectFrom(member)
                .where(
                        eqId(id),
                        isActive()
                )
                .fetchOne();
        return Optional.ofNullable(activeMember)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private BooleanExpression isActive() {
        return member.deletedAt.isNull();
    }

    private BooleanExpression eqEmail(String email) {
        return member.email.eq(email);
    }

    private BooleanExpression eqId(Long id) {
        return member.id.eq(id);
    }

}
