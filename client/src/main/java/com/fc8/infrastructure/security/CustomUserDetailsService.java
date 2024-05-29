package com.fc8.infrastructure.security;

import com.fc8.platform.common.exception.AuthenticationException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.getByEmail(username);

        AptnerMember aptnerMember = new AptnerMember(member);
        validateAuthenticate(aptnerMember);

        return aptnerMember;
    }

    private void validateAuthenticate(AptnerMember member) {
        if (member == null) {
            throw new AuthenticationException(ErrorCode.INTERNAL_AUTHENTICATION_SERVICE);
        }
        validateEnabled(member);
        validateAccountExpired(member);
        validateAccountNonLocked(member);
        validateCredentialNonExpired(member);
    }

    private static void validateEnabled(AptnerMember member) {
        if(!member.isEnabled()){
            throw new AuthenticationException(ErrorCode.DISABLE_ACCOUNT);
        }
    }

    private static void validateCredentialNonExpired(AptnerMember member) {
        if (!member.isCredentialsNonExpired()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountNonLocked(AptnerMember member) {
        if (!member.isAccountNonLocked()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountExpired(AptnerMember member) {
        if (!member.isAccountNonExpired()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

}
