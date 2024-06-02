package com.fc8.platform.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangePasswordCommand {

    private final String currentPassword;
    private final String newPassword;
    private final String confirmNewPassword;

}
