package com.fc8.platform.repository;


import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.MemberBlock;

public interface MemberBlockRepository {

    MemberBlock store(MemberBlock memberBlock);

    void delete(MemberBlock memberBlock);

    MemberBlock getByMemberAndBlocked(Member member, Member blocked);

    boolean existsByMemberAndBlocked(Member blocker, Member blocked);
}
