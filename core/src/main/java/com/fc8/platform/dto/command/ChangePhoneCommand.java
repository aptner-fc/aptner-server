package com.fc8.platform.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangePhoneCommand {

    private final String phone;
    private final String verificationCode;
    private final String newPhone;

}
