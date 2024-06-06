package com.fc8.platform.dto.response;

import com.fc8.platform.domain.enums.Gender;
import com.fc8.platform.dto.record.MemberInfo;
import lombok.Getter;

@Getter
public class LoadMyProfileResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final String nickname;
    private final String phone;
    private final String profileImage;
    private final Gender gender;

    public LoadMyProfileResponse(Long id, String email, String name, String nickname, String phone, String profileImage, Gender gender) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.profileImage = profileImage;
        this.gender = gender;
    }

    public LoadMyProfileResponse(MemberInfo memberInfo) {
        this.id = memberInfo.id();
        this.email = memberInfo.email();
        this.name = memberInfo.name();
        this.nickname = memberInfo.nickname();
        this.phone = memberInfo.phone();
        this.profileImage = memberInfo.profileImage();
        this.gender = memberInfo.gender();
    }


}
