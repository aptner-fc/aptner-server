package com.fc8.application;

import com.fc8.facade.MemberFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.request.SignInMemberRequest;
import com.fc8.platform.dto.request.SignUpMemberRequest;
import com.fc8.platform.dto.response.SendSMSCodeResponse;
import com.fc8.platform.dto.response.SignInMemberResponse;
import com.fc8.platform.dto.response.SignUpMemberResponse;
import com.fc8.platform.dto.response.VerifySMSCodeResponse;
import com.fc8.platform.mapper.MemberMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 관련 API", description = "회원 관련 API 모음 입니다.")
@RestController
@RequestMapping(value = {"/v1/api/members"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberMapper memberMapper;
    private final MemberFacade memberFacade;

    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다.")
    @PostMapping(value = "/sign-up")
    public ResponseEntity<CommonResponse<SignUpMemberResponse>> signUp(@Valid @RequestBody SignUpMemberRequest request) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.signUp(command));
    }

    @Operation(summary = "로그인 API", description = "로그인 API 입니다.")
    @PostMapping(value = "/sign-in")
    public ResponseEntity<CommonResponse<SignInMemberResponse>> signIn(@Valid @RequestBody SignInMemberRequest request) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.signIn(command));
    }

    @Operation(summary = "인증 메세지 발급 API", description = "인증 메세지 발급 API 입니다.")
    @GetMapping(value = "/send-verification")
    public ResponseEntity<CommonResponse<SendSMSCodeResponse>> sendVerificationCode(@NotNull(message = "전화번호가 누락되었습니다.") @RequestParam("phone") String phone) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.sendVerificationCode(phone));
    }

    @Operation(summary = "인증 메세지 확인 API", description = "인증 메세지 확인 API 입니다.")
    @GetMapping(value = "/verify-code")
    public ResponseEntity<CommonResponse<VerifySMSCodeResponse>> verifyCode(@NotNull(message = "전화번호가 누락되었습니다.") @RequestParam String phone,
                                                                            @NotNull(message = "인증 코드가 누락되었습니다.") @RequestParam String verificationCode) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.verifyCode(phone, verificationCode));
    }

}
