package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.MemberFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.dto.request.*;
import com.fc8.platform.dto.response.*;
import com.fc8.platform.mapper.MemberMapper;
import com.fc8.platform.mapper.PageMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "회원 관련 API", description = "회원 관련 API 모음 입니다.")
@RestController
@RequestMapping(value = {"/v1/api/members"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberMapper memberMapper;
    private final MemberFacade memberFacade;
    private final PageMapper pageMapper;

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
    public ResponseEntity<CommonResponse<SendSMSCodeResponse>> sendVerificationCode(@Valid @NotNull(message = "전화번호가 누락되었습니다.") @RequestParam("phone") String phone) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.sendVerificationCode(phone));
    }

    @Operation(summary = "인증 메세지 확인 API", description = "인증 메세지 확인 API 입니다.")
    @GetMapping(value = "/verify-code")
    public ResponseEntity<CommonResponse<VerifySMSCodeResponse>> verifyCode(@Valid @NotNull(message = "전화번호가 누락되었습니다.") @RequestParam String phone,
                                                                            @Valid @NotNull(message = "인증 코드가 누락되었습니다.") @RequestParam String verificationCode) {
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.verifyCode(phone, verificationCode));
    }

    @Operation(summary = "내가 작성한 글 확인 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/posts")
    public ResponseEntity<CommonResponse<PageResponse<LoadMyArticleListResponse>>> loadMyArticleLis(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentMember CurrentMember currentMember,
            CustomPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.loadMyArticleLis(currentMember.id(), apartCode, command));
    }

    @Operation(summary = "내가 작성한 글 삭제 API")
    @CheckApartType
    @DeleteMapping(value = "/{apartCode}/posts")
    public ResponseEntity<CommonResponse<DeleteMyArticleListResponse>> deleteMyArticleLis(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestBody DeleteMyArticleListRequest request
    ) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.deleteMyArticleList(currentMember.id(), apartCode, command));
    }

    @Operation(summary = "내가 작성한 댓글 확인 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/comments")
    public ResponseEntity<CommonResponse<PageResponse<LoadMyCommentListResponse>>> loadMyCommentList(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentMember CurrentMember currentMember,
            CustomPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.loadMyCommentList(currentMember.id(), apartCode, command));
    }

    @Operation(summary = "내가 작성한 댯글 삭제 API")
    @CheckApartType
    @DeleteMapping(value = "/{apartCode}/comments")
    public ResponseEntity<CommonResponse<DeleteMyCommentListResponse>> deleteMyArticleLis(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestBody DeleteMyCommentListRequest request
    ) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.deleteMyCommentList(currentMember.id(), apartCode, command));
    }

    @Operation(summary = "기본 정보 변경 API")
    @PatchMapping(value = "/my-pages/profile")
    public ResponseEntity<CommonResponse<ModifyProfileResponse>> modifyProfile(
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") ModifyProfileRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.modifyProfile(currentMember.id(), command, image));
    }

    @Operation(summary = "비밀 번호 변경 API")
    @PatchMapping(value = "/my-pages/password")
    public ResponseEntity<CommonResponse<ChangePasswordResponse>> changePassword(
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestBody ChangePasswordRequest request) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.changePassword(currentMember.id(), command));
    }

    @Operation(summary = "인증 정보 변경 API")
    @PatchMapping(value = "/my-pages/phone")
    public ResponseEntity<CommonResponse<ChangePhoneResponse>> changePhone(
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestBody ChangePhoneRequest request) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.changePhone(currentMember.id(), command));
    }

    @Operation(summary = "아이디 찾기 API")
    @GetMapping(value = "/{apartCode}/auth/email")
    public ResponseEntity<CommonResponse<FindEmailResponse>> findEmail(
            @NotNull @PathVariable String apartCode,
            FindEmailRequest request
    ) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.findEmail(apartCode, command));
    }

    @Operation(summary = "비밀번호 변경 API")
    @PatchMapping(value = "/{apartCode}/auth/password")
    public ResponseEntity<CommonResponse<ModifyPasswordResponse>> modifyPassword(
            @NotNull @PathVariable String apartCode,
            @Valid @RequestBody ModifyPasswordRequest request
    ) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.modifyPassword(apartCode, command));
    }

    @Operation(summary = "회원 차단 API")
    @PostMapping(value = "/block")
    public ResponseEntity<CommonResponse<BlockMemberResponse>> blockMember(
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestBody BlockMemberRequest request
    ) {
        var command = memberMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.blockMember(currentMember.id(), command));
    }

//    @Operation(summary = "회원 목록 조회 API")
//    @PostMapping(value = "/block")
//    public ResponseEntity<CommonResponse<LoadBlockedMemberResponse>> loadBlockedMember(
//            @CheckCurrentMember CurrentMember currentMember,
//            @Valid @RequestBody LoadBlockedMemberRequest request
//    ) {
//        var command = memberMapper.of(request);
//        return CommonResponse.success(SuccessCode.SUCCESS, memberFacade.loadBlockedMember(currentMember.id(), command));
//    }

}
