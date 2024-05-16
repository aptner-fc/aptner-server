package com.fc8.facade;

import com.fc8.platform.dto.SignInAdminInfo;
import com.fc8.platform.dto.command.SignInAdminCommand;
import com.fc8.platform.dto.command.SignUpAdminCommand;
import com.fc8.platform.dto.response.SignInAdminResponse;
import com.fc8.platform.dto.response.SignUpAdminResponse;
import com.fc8.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminFacade {

    private final AdminService adminService;

    public SignUpAdminResponse signUp(SignUpAdminCommand command) {
        return new SignUpAdminResponse(adminService.signUp(command));
    }

    public SignInAdminResponse signIn(SignInAdminCommand command) {
        SignInAdminInfo signInAdminInfo = adminService.signIn(command);
        return new SignInAdminResponse(new SignInAdminResponse.SignInAdmin(signInAdminInfo.adminInfo()), signInAdminInfo.tokenInfo());
    }
}
