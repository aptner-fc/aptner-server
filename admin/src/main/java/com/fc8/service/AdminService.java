package com.fc8.service;

import com.fc8.platform.dto.SignInAdminInfo;
import com.fc8.platform.dto.command.SignInAdminCommand;
import com.fc8.platform.dto.command.SignUpAdminCommand;

public interface AdminService {

    Long signUp(SignUpAdminCommand command);

    SignInAdminInfo signIn(SignInAdminCommand command);
}
