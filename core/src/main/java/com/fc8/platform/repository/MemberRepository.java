package com.fc8.platform.repository;


import com.fc8.platform.domain.entity.member.Member;

public interface MemberRepository {

    Member store(Member member);

    Member getActiveMemberById(Long id);

    Member getByEmail(String email);

    boolean existActiveEmail(String email);

    boolean existNickname(String nickname);

}
