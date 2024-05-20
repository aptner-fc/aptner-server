package com.fc8.infrastructure.security;

import com.fc8.platform.common.exception.AuthenticationException;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.enums.ApartType;
import com.fc8.platform.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.getByEmail(username);
        ApartType apartType = adminRepository.getApartTypeByAdmin(admin);

        validateAdminApart(apartType);

        AptnerAdmin aptnerAdmin = new AptnerAdmin(admin, apartType);
        validateAuthenticate(aptnerAdmin);

        return aptnerAdmin;
    }

    private void validateAdminApart(ApartType apartType) {
        if (apartType == null) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_ADMIN_APART);
        }
    }

    private void validateAuthenticate(AptnerAdmin member) {
        if (member == null) {
            throw new AuthenticationException(ErrorCode.INTERNAL_AUTHENTICATION_SERVICE);
        }
        validateEnabled(member);
        validateAccountExpired(member);
        validateAccountNonLocked(member);
        validateCredentialNonExpired(member);
    }

    private static void validateEnabled(AptnerAdmin member) {
        if(!member.isEnabled()){
            throw new AuthenticationException(ErrorCode.DISABLE_ACCOUNT);
        }
    }

    private static void validateCredentialNonExpired(AptnerAdmin member) {
        if (!member.isCredentialsNonExpired()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountNonLocked(AptnerAdmin member) {
        if (!member.isAccountNonLocked()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountExpired(AptnerAdmin member) {
        if (!member.isAccountNonExpired()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

}
