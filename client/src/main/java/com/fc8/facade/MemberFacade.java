package com.fc8.facade;

import com.fc8.platform.dto.command.*;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.dto.response.*;
import com.fc8.service.ApartService;
import com.fc8.service.MemberService;
import com.fc8.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public ModifyProfileResponse modifyProfile(Long memberId, ModifyProfileCommand command, MultipartFile image) {
        return new ModifyProfileResponse(memberService.modifyProfile(memberId, command, image));
    }

    public ChangePasswordResponse changePassword(Long memberId, ChangePasswordCommand command) {
        return new ChangePasswordResponse(memberService.changePassword(memberId, command));
    }

    public ChangePhoneResponse changePhone(Long memberId, ChangePhoneCommand command) {
        return new ChangePhoneResponse(memberService.changePhone(memberId, command));
    }

    public DeleteMyArticleListResponse deleteMyArticleList(Long memberId, String apartCode, DeleteMyArticleListCommand command) {
        return new DeleteMyArticleListResponse(memberService.deleteMyArticleList(memberId, apartCode, command));
    }

    public DeleteMyCommentListResponse deleteMyCommentList(Long memberId, String apartCode, DeleteMyCommentListCommand command) {
        return new DeleteMyCommentListResponse(memberService.deleteMyCommentList(memberId, apartCode, command));
    }

    public LoadMyProfileResponse loadProfile(Long memberId, String apartCode) {
        return new LoadMyProfileResponse(memberService.loadProfile(memberId, apartCode));
    }

    public FindEmailResponse findEmail(String apartCode, FindEmailCommand command) {
        return new FindEmailResponse(memberService.findEmail(apartCode, command));
    }

    public CheckEmailResponse checkEmail(String apartCode, CheckEmailCommand command) {
        return new CheckEmailResponse(memberService.checkEmail(apartCode, command));
    }

    public ModifyPasswordResponse modifyPassword(String apartCode, ModifyPasswordCommand command) {
        return new ModifyPasswordResponse(memberService.modifyPassword(apartCode, command));
    }

    public BlockMemberResponse blockMember(Long memberId, String apartCode, BlockMemberCommand command) {
        return new BlockMemberResponse(memberService.blockMember(memberId, apartCode, command));
    }

    public UnBlockMemberResponse unBlockMember(Long memberId, String apartCode, UnBlockMemberCommand command) {
        return new UnBlockMemberResponse(memberService.unBlockMember(memberId, apartCode, command));
    }

    public PageResponse<LoadBlockedMemberResponse> loadBlockedMember(Long memberId, String apartCode, CustomPageCommand command) {
        final Page<MemberSummary> myList = memberService.loadBlockedMember(memberId, apartCode, command);
        return new PageResponse<>(myList, new LoadBlockedMemberResponse(myList.getContent()));
    }
}
