package com.fc8.server.impl;

import com.fc8.platform.domain.entity.member.MemberAuth;
import com.fc8.platform.domain.entity.member.QMemberAuth;
import com.fc8.platform.repository.MemberAuthRepository;
import com.fc8.server.MemberAuthJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberAuthRepositoryImpl implements MemberAuthRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberAuthJpaRepository memberAuthJpaRepository;

    QMemberAuth memberAuth = QMemberAuth.memberAuth;

    @Override
    public MemberAuth store(MemberAuth memberAuth) {
        return memberAuthJpaRepository.save(memberAuth);
    }

}
