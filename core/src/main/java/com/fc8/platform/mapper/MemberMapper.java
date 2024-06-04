package com.fc8.platform.mapper;

import com.fc8.platform.dto.command.*;
import com.fc8.platform.dto.request.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MemberMapper {

    SignUpMemberCommand of(SignUpMemberRequest request);

    SignInMemberCommand of(SignInMemberRequest request);

    ModifyProfileCommand of(ModifyProfileRequest request);

    ChangePasswordCommand of(ChangePasswordRequest request);

    ChangePhoneCommand of(ChangePhoneRequest request);

    DeleteMyArticleListCommand of(DeleteMyArticleListRequest request);

    DeleteMyCommentListCommand of(DeleteMyCommentListRequest request);

    FindEmailCommand of(FindEmailRequest request);

    ModifyPasswordCommand of(ModifyPasswordRequest request);

}
