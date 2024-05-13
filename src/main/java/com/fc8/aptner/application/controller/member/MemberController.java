package com.fc8.aptner.application.controller.member;

import com.fc8.aptner.common.exception.code.SuccessCode;
import com.fc8.aptner.common.response.CommonResponse;
import com.fc8.aptner.core.dto.request.SignInMemberRequest;
import com.fc8.aptner.core.dto.request.SignUpMemberRequest;
import com.fc8.aptner.core.dto.response.SignInMemberResponse;
import com.fc8.aptner.core.dto.response.SignUpMemberResponse;
import com.fc8.aptner.core.facade.MemberFacade;
import com.fc8.aptner.core.mapper.MemberMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
