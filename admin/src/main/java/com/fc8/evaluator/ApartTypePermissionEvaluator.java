package com.fc8.evaluator;

import com.fc8.platform.converter.ApartTypeConverter;
import com.fc8.platform.domain.enums.ApartType;
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

    public boolean hasPermission(Authentication authentication, String type) {
        // 1. 인증 정보에서 사용자 권한 획득
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 2. 아파트 타입 조회
        ApartType apartType = apartTypeConverter.convertToEntityAttribute(type);

        // 3. 권한 비교
        return authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + apartType.name()));
    }

}
