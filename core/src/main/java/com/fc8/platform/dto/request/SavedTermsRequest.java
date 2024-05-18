package com.fc8.platform.dto.request;

import com.fc8.platform.domain.enums.TermsType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SavedTermsRequest {

    @NotBlank(message = "약관 제목이 누락되었습니다.")
    private String title;

    @NotBlank(message = "약관 내용이 누락되었습니다.")
    private String content;

    @NotNull(message = "약관 타입이 누락되었습니다.")
    private TermsType type;

    @Setter
    @NotNull(message = "사용 여부가 누락되었습니다.")
    private boolean isUsed;

    @Setter
    @NotNull(message = "필수 여부가 누락되었습니다.")
    private boolean isRequired;

}

