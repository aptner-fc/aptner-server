package com.fc8.platform.mapper;

import com.fc8.platform.dto.command.SignInMemberCommand;
import com.fc8.platform.dto.command.SignUpMemberCommand;
import com.fc8.platform.dto.request.SignInMemberRequest;
import com.fc8.platform.dto.request.SignUpMemberRequest;
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
