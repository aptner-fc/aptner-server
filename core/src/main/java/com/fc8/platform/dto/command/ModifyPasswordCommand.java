package com.fc8.platform.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyPasswordCommand {

    private final String email;
    private final String name;
    private final String phone;
    private final String verificationCode;
    private final String password;
    private final String confirmPassword;
}
