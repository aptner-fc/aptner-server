package com.fc8.platform.mapper;

import com.fc8.platform.dto.command.SignInAdminCommand;
import com.fc8.platform.dto.command.SignUpAdminCommand;
import com.fc8.platform.dto.command.WriteQnaAnswerCommand;
import com.fc8.platform.dto.request.SignInAdminRequest;
import com.fc8.platform.dto.request.SignUpAdminRequest;
import com.fc8.platform.dto.request.WriteQnaAnswerRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AdminMapper {

    SignUpAdminCommand of(SignUpAdminRequest request);

    SignInAdminCommand of(SignInAdminRequest request);

    WriteQnaAnswerCommand of (WriteQnaAnswerRequest request);

}
