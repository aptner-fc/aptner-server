package com.fc8.aptner.core.repository;

import com.fc8.aptner.core.domain.entity.member.Member;

public interface MemberRepository {

    Member store(Member member);

    Member getMemberByEmail(String email);

    boolean existActiveEmail(String email);

    boolean existNickname(String nickname);

}
