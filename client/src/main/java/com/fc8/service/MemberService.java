package com.fc8.service;


import com.fc8.platform.dto.command.*;
import com.fc8.platform.dto.record.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    Long signUp(SignUpMemberCommand command);

    SignInMemberInfo signIn(SignInMemberCommand command);

    Page<LoadMyArticleInfo> loadMyArticleList(Long memberId, String apartCode, CustomPageCommand command);

    Page<LoadMyCommentInfo> loadMyCommentList(Long memberId, String apartCode, CustomPageCommand command);

    MemberSummary modifyProfile(Long memberId, ModifyProfileCommand command, MultipartFile image);

    MemberSummary changePassword(Long memberId, ChangePasswordCommand command);

    MemberSummary changePhone(Long memberId, ChangePhoneCommand command);

    DeletedCountInfo deleteMyArticleList(Long memberId, String apartCode, DeleteMyArticleListCommand command);

    DeletedCountInfo deleteMyCommentList(Long memberId, String apartCode, DeleteMyCommentListCommand command);

    MemberInfo loadProfile(Long memberId, String apartCode);

    MemberInfo findEmail(String apartCode, FindEmailCommand command);

    MemberInfo checkEmail(String apartCode, CheckEmailCommand command);

    MemberInfo modifyPassword(String apartCode, ModifyPasswordCommand command);

    MemberInfo blockMember(Long memberId, String apartCode, BlockMemberCommand command);

    MemberInfo unBlockMember(Long memberId, String apartCode, UnBlockMemberCommand command);

    Page<MemberSummary> loadBlockedMember(Long memberId, String apartCode, CustomPageCommand command);

    Page<NotificationInfo> loadNotificationList(Long memberId, CustomPageCommand command);
}
