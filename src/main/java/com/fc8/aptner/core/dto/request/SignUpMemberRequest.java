package com.fc8.aptner.core.dto.request;

import com.fc8.aptner.core.domain.enums.Gender;
import com.fc8.aptner.core.dto.ApartmentInfo;
import com.fc8.aptner.core.dto.TermsAgreement;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpMemberRequest {

    @NotBlank(message = "이메일이 누락되었습니다.")
    @Email(message = "이메일 주소 형식이 잘못되었습니다.")
    private String email;

    @NotBlank(message = "이름이 누락되었습니다.")
    private String name;

    @NotBlank(message = "닉네임이 누락되었습니다.")
    private String nickname;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?!.*\\s).+$"
            , message = "비밀번호는 영어와 숫자를 혼용해야 하며 공백은 사용할 수 없습니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 최소 8글자 이상 최대 16글자 이하로 작성해야 합니다.")
    private String password;

    private String phone;

    @NotNull
    private Gender gender;

    private ApartmentInfo apartmentInfo;

    private List<TermsAgreement> termsAgreements;
}
