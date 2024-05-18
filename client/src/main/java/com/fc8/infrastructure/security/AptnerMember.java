package com.fc8.infrastructure.security;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.enums.MemberStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class AptnerMember implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.valueOf(member.getRole().name()));
        return Collections.singleton(grantedAuthority);
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return member.getStatus() != MemberStatus.WITHDRAWAL;
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.getStatus() != MemberStatus.BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return member.getStatus() == MemberStatus.ACTIVE;
    }
}
