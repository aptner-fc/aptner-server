package com.fc8.platform.dto.command;

import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.enums.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpAdminCommand {

    private final String email;

    private final String name;

    private final String nickname;

    private final String password;

    private final String phone;

    private final Gender gender;

    private final String apartCode;

    public Admin toEntity(String encPassword, Apart apart) {
        return Admin.create(email, name, nickname, encPassword, phone, gender, apart);
    }

}
