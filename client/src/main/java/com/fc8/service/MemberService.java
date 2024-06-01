package com.fc8.service;


import com.fc8.platform.dto.command.SignInMemberCommand;
import com.fc8.platform.dto.command.SignUpMemberCommand;
import com.fc8.platform.dto.record.CustomPageCommand;
import com.fc8.platform.dto.record.LoadMyArticleInfo;
import com.fc8.platform.dto.record.LoadMyCommentInfo;
import com.fc8.platform.dto.record.SignInMemberInfo;
import org.springframework.data.domain.Page;

public interface MemberService {

    Long signUp(SignUpMemberCommand command);

    SignInMemberInfo signIn(SignInMemberCommand command);

    Page<LoadMyArticleInfo> loadMyArticleList(Long memberId, String apartCode, CustomPageCommand command);

    Page<LoadMyCommentInfo> loadMyCommentList(Long memberId, String apartCode, CustomPageCommand command);

}
