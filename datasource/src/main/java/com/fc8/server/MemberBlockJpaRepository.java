package com.fc8.server;


import com.fc8.platform.domain.entity.member.MemberBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBlockJpaRepository extends JpaRepository<MemberBlock, Long> {

}
