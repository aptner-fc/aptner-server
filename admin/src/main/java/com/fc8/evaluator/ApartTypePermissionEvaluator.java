package com.fc8.evaluator;

import com.fc8.platform.common.exception.AuthenticationException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.converter.ApartTypeConverter;
import com.fc8.platform.domain.enums.ApartType;
import com.fc8.platform.dto.record.ApartInfo;
import com.fc8.platform.dto.record.CurrentAdmin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ApartTypePermissionEvaluator {

    private final ApartTypeConverter apartTypeConverter;

    public ApartTypePermissionEvaluator() {
        this.apartTypeConverter = new ApartTypeConverter();
    }

    public boolean hasPermission(Authentication authentication, String typeCode) {
        // 1. 인증 정보에서 사용자 권한 획득
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 2. 아파트 타입 조회
        ApartType apartType = apartTypeConverter.convertToEntityAttribute(typeCode);

        // 3. 권한 비교
        boolean affected = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + apartType.name()));
        if (!affected) {
            throw new AuthenticationException(ErrorCode.NOT_PERMISSION_APART);
        }

        return true;
    }

    public boolean hasPermission(CurrentAdmin currentAdmin, String apartCode) {
        // 1. 인증 정보에서 사용자 권한 획득
        ApartInfo apartInfo = currentAdmin.apartInfo();

        // 2. 회원 아파트 검증
        boolean affected = apartInfo.code().equals(apartCode);
        if (!affected) {
            throw new AuthenticationException(ErrorCode.NOT_PERMISSION_APART);
        }

        return true;
    }

}
