package com.fc8.aptner.datasource.repository.impl;

import com.fc8.aptner.common.exception.InvalidParamException;
import com.fc8.aptner.core.domain.member.Member;
import com.fc8.aptner.core.repository.MemberRepository;
import com.fc8.aptner.datasource.repository.MemberJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final JPAQueryFactory queryFactory;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member getMemberByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(InvalidParamException::new);
    }


}
