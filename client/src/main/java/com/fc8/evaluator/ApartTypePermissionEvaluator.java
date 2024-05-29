package com.fc8.evaluator;

import com.fc8.platform.common.exception.AuthenticationException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.dto.record.CurrentMember;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApartTypePermissionEvaluator {

    public boolean hasPermission(CurrentMember currentMember, String apartCode) {
        // 1. 인증 정보에서 사용자 권한 획득
        CurrentMember loginMember = Optional.ofNullable(currentMember)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_AUTHENTICATION));

        var mainApartInfo = loginMember.mainApartInfo();
        var apartInfoList = loginMember.apartInfoList();

        // 2. 회원의 아파트 검증
        boolean affected =
                mainApartInfo.code().equals(apartCode) ||
                apartInfoList.stream().anyMatch(apartInfo -> apartInfo.code().equals(apartCode));

        if (!affected) {
            throw new AuthenticationException(ErrorCode.NOT_PERMISSION_APART);
        }

        return true;
    }

}
