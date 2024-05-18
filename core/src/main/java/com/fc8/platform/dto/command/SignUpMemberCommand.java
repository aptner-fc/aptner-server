package com.fc8.platform.dto.command;


import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.enums.Gender;
import com.fc8.platform.dto.ApartmentInfo;
import com.fc8.platform.dto.TermsAgreement;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SignUpMemberCommand {

    private final String email;

    private final String name;

    private final String nickname;

    private final String password;

    private final String phone;

    private final Gender gender;

    private final ApartmentInfo apartment;

    private final List<TermsAgreement> termsAgreements;

    public Member toEntity(String encPassword) {
        return Member.create(email, name, nickname, encPassword, phone, gender);
    }

}
