package com.fc8.aptner.core.mapper;

import com.fc8.aptner.core.dto.command.SignInMemberCommand;
import com.fc8.aptner.core.dto.command.SignUpMemberCommand;
import com.fc8.aptner.core.dto.request.SignInMemberRequest;
import com.fc8.aptner.core.dto.request.SignUpMemberRequest;
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

}
