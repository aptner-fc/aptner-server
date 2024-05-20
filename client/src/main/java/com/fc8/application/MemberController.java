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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/v1/api/member", "/api/member"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberMapper memberMapper;
    private final MemberFacade memberFacade;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<CommonResponse<SignUpMemberResponse>> signUp(@Valid @RequestBody SignUpMemberRequest request) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.signUp(command));
    }

    @PostMapping(value = "/sign-in")
    public ResponseEntity<CommonResponse<SignInMemberResponse>> signIn(@Valid @RequestBody SignInMemberRequest request) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.signIn(command));
    }

    @GetMapping(value = "/send-verification")
    public ResponseEntity<CommonResponse<SendSMSCodeResponse>> sendVerificationCode(String phone) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.sendVerificationCode(phone));
    }

//    @GetMapping(value = "/verify-code")
//    public ResponseEntity<CommonResponse<VerifySMSCodeResponse>> verifyCode(String verificationCode) {
//        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.verifyCode(verificationCode));
//    }

}
