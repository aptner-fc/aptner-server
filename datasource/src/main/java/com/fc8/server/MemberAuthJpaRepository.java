package com.fc8.server;

import com.fc8.platform.domain.entity.member.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAuthJpaRepository extends JpaRepository<MemberAuth, Long> {
}
