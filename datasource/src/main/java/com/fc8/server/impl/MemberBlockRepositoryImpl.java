package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.MemberBlock;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.member.QMemberBlock;
import com.fc8.platform.repository.MemberBlockRepository;
import com.fc8.server.MemberBlockJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberBlockRepositoryImpl implements MemberBlockRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberBlockJpaRepository memberBlockJpaRepository;

    QMemberBlock memberBlock = QMemberBlock.memberBlock;

    @Override
    public MemberBlock store(MemberBlock memberBlock) {
        return memberBlockJpaRepository.save(memberBlock);
    }

    @Override
    public void delete(MemberBlock memberBlock) {
        memberBlockJpaRepository.delete(memberBlock);
    }

    @Override
    public MemberBlock getByMemberAndBlocked(Member member, Member blocked) {
        MemberBlock block = jpaQueryFactory
                .selectFrom(memberBlock)
                .where(
                        eqMember(memberBlock.member, member),
                        eqMember(memberBlock.blocked, blocked)
                )
                .fetchOne();

        return Optional.ofNullable(block)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_BLOCK));
    }

    @Override
    public boolean existsByMemberAndBlocked(Member blocker, Member blocked) {
        return jpaQueryFactory
                .selectOne()
                .from(memberBlock)
                .where(
                        memberBlock.member.eq(blocker).and(memberBlock.blocked.eq(blocked))
                )
                .fetchFirst() != null;
    }

    private BooleanExpression eqMember(QMember member, Member activeMember) {
        return member.eq(activeMember);
    }

}
