package com.fc8.facade;

import com.fc8.platform.dto.command.SignInMemberCommand;
import com.fc8.platform.dto.command.SignUpMemberCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.dto.response.*;
import com.fc8.service.ApartService;
import com.fc8.service.MemberService;
import com.fc8.service.PostService;
import com.fc8.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;
    private final ApartService apartService;
    private final PostService postService;
    private final SMSService smsService;

    public SignUpMemberResponse signUp(SignUpMemberCommand command) {
        final Long memberId = memberService.signUp(command);
        final String contact = apartService.getContactByCode(command.getApartment().code());
        return new SignUpMemberResponse(memberId, contact);
    }

    public SignInMemberResponse signIn(SignInMemberCommand command) {
        final SignInMemberInfo signInMemberInfo = memberService.signIn(command);
        return new SignInMemberResponse(new SignInMemberResponse.SignInMember(signInMemberInfo.memberInfo()), signInMemberInfo.tokenInfo());
    }

    public SendSMSCodeResponse sendVerificationCode(String phone) {
        final SMSVerificationCode smsVerificationCode = smsService.sendVerificationCode(phone);
        return new SendSMSCodeResponse(smsVerificationCode.verificationCode(), smsVerificationCode.expiredAt());
    }

    public VerifySMSCodeResponse verifyCode(String phone, String verificationCode) {
        final SMSVerificationInfo smsVerificationInfo = smsService.verifyCode(phone, verificationCode);
        return smsVerificationInfo.isVerify() ?
                VerifySMSCodeResponse.verify(smsVerificationInfo.verificationCode(), smsVerificationInfo.expiredAt()) :
                VerifySMSCodeResponse.fail(smsVerificationInfo.attempt());

    }

    public PageResponse<LoadMyArticleListResponse> loadMyArticleLis(Long memberId, String apartCode, CustomPageCommand command) {
        final Page<LoadMyArticleInfo> myList = memberService.loadMyArticleList(memberId, apartCode, command);
        return new PageResponse<>(myList, new LoadMyArticleListResponse(myList.getContent()));
    }

    public PageResponse<LoadMyCommentListResponse> loadMyCommentList(Long memberId, String apartCode, CustomPageCommand command) {
        final Page<LoadMyCommentInfo> myList = memberService.loadMyCommentList(memberId, apartCode, command);
        return new PageResponse<>(myList, new LoadMyCommentListResponse(myList.getContent()));
    }
}
