package com.fc8.platform.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyProfileCommand {

    private final String nickname;

}
