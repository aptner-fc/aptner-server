package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.MemberAuth;

public interface MemberAuthRepository {

    MemberAuth store(MemberAuth memberAuth);
}
