package com.fc8.server.impl;

import com.fc8.platform.domain.entity.member.MemberBlock;
import com.fc8.platform.repository.MemberBlockRepository;
import com.fc8.server.MemberBlockJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberBlockRepositoryImpl implements MemberBlockRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberBlockJpaRepository memberBlockJpaRepository;

    @Override
    public MemberBlock store(MemberBlock memberBlock) {
        return memberBlockJpaRepository.save(memberBlock);
    }

}
