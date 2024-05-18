package com.fc8.facade;

import com.fc8.platform.dto.SignInMemberInfo;
import com.fc8.platform.dto.command.SignInMemberCommand;
import com.fc8.platform.dto.command.SignUpMemberCommand;
import com.fc8.platform.dto.response.SignInMemberResponse;
import com.fc8.platform.dto.response.SignUpMemberResponse;
import com.fc8.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;

    public SignUpMemberResponse signUp(SignUpMemberCommand command) {
        return new SignUpMemberResponse(memberService.signUp(command));
    }

    public SignInMemberResponse signIn(SignInMemberCommand command) {
        SignInMemberInfo signInMemberInfo = memberService.signIn(command);
        return new SignInMemberResponse(new SignInMemberResponse.SignInMember(signInMemberInfo.memberInfo()), signInMemberInfo.tokenInfo());
    }
}
