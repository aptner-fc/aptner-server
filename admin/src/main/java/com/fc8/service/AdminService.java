package com.fc8.service;

import com.fc8.platform.dto.record.ApartInfo;
import com.fc8.platform.dto.record.AuthMemberInfo;
import com.fc8.platform.dto.record.SignInAdminInfo;
import com.fc8.platform.dto.command.SignInAdminCommand;
import com.fc8.platform.dto.command.SignUpAdminCommand;

public interface AdminService {

    Long signUp(SignUpAdminCommand command);

    SignInAdminInfo signIn(SignInAdminCommand command);

    AuthMemberInfo authenticateMember(Long adminId, Long memberId, ApartInfo apartInfo);
}
