package com.fc8.aptner.core.dto.command;

import com.fc8.aptner.core.domain.entity.member.Member;
import com.fc8.aptner.core.domain.enums.Gender;
import com.fc8.aptner.core.dto.ApartmentInfo;
import com.fc8.aptner.core.dto.TermsAgreement;
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

    private final ApartmentInfo apartmentInfo;

    private final List<TermsAgreement> termsAgreements;

    public Member toEntity(String encPassword) {
        var apart = com.fc8.aptner.core.domain.entity.apartment.Apartment.build(apartmentInfo.dong(), apartmentInfo.ho());
        return Member.create(email, name, nickname, encPassword, phone, gender, apart);
    }
}
