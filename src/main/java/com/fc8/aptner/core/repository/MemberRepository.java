package com.fc8.aptner.core.repository;

import com.fc8.aptner.core.domain.member.Member;

public interface MemberRepository {

    Member getMemberByEmail(String email);
}
