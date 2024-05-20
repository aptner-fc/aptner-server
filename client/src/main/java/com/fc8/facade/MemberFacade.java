package com.fc8.facade;

import com.fc8.platform.dto.command.SignInMemberCommand;
import com.fc8.platform.dto.command.SignUpMemberCommand;
import com.fc8.platform.dto.record.SMSVerification;
import com.fc8.platform.dto.record.SignInMemberInfo;
import com.fc8.platform.dto.response.SendSMSCodeResponse;
import com.fc8.platform.dto.response.SignInMemberResponse;
import com.fc8.platform.dto.response.SignUpMemberResponse;
import com.fc8.service.ApartService;
import com.fc8.service.MemberService;
import com.fc8.service.impl.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;
    private final ApartService apartService;
    private final SMSService smsService;

    public SignUpMemberResponse signUp(SignUpMemberCommand command) {
        final Long memberId = memberService.signUp(command);
        final String contact = apartService.getContactByCode(command.getApartment().code());
        return new SignUpMemberResponse(memberId, contact);
    }

    public SignInMemberResponse signIn(SignInMemberCommand command) {
        SignInMemberInfo signInMemberInfo = memberService.signIn(command);
        return new SignInMemberResponse(new SignInMemberResponse.SignInMember(signInMemberInfo.memberInfo()), signInMemberInfo.tokenInfo());
    }

    public SendSMSCodeResponse sendVerificationCode(String phone) {
        SMSVerification smsVerification = smsService.sendVerificationCode(phone);
        return new SendSMSCodeResponse(smsVerification.verificationCode(), smsVerification.expiredAt());
    }
}
