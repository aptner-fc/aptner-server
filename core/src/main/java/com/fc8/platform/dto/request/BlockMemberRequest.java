package com.fc8.platform.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockMemberRequest {

    @NotNull(message = "차단할 회원 정보가 누락되었습니다.")
    private Long blockedMemberId;

}
