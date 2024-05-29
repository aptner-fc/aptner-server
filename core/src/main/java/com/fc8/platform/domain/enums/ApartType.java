package com.fc8.platform.domain.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApartType implements CodeEnum {

    LEADERS_ONE("AT000", "리더스원")
    ;

    private final String code;
    private final String name;

}
