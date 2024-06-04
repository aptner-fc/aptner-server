package com.fc8.platform.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindEmailCommand {

    private final String name;
    private final String phone;
}
