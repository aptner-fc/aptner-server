package com.fc8.platform.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckEmailCommand {

    private final String email;
    private final String name;
    private final String phone;
}
