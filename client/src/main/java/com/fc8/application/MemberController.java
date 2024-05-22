package com.fc8.application;

import com.fc8.facade.MemberFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.request.SignInMemberRequest;
import com.fc8.platform.dto.request.SignUpMemberRequest;
import com.fc8.platform.dto.response.SendSMSCodeResponse;
import com.fc8.platform.dto.response.SignInMemberResponse;
import com.fc8.platform.dto.response.SignUpMemberResponse;
import com.fc8.platform.mapper.MemberMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 관련 API", description = "회원 관련 API 모음 입니다.")
@RestController
@RequestMapping(value = {"/v1/api/member"})
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

    @Operation(summary = "댓글 작성 API", description = "댓글 작성 API 입니다.")
    @GetMapping(value = "/send-verification")
    public ResponseEntity<CommonResponse<SendSMSCodeResponse>> sendVerificationCode(@RequestParam String phone) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.sendVerificationCode(phone));
    }

//    @GetMapping(value = "/verify-code")
//    public ResponseEntity<CommonResponse<VerifySMSCodeResponse>> verifyCode(String verificationCode) {
//        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.verifyCode(verificationCode));
//    }

}
