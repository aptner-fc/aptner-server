package com.fc8.aptner.core.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TermsType implements CodeEnum {

    SERVICE("TR000", "서비스 이용"),
    PRIVACY("TR001", "개인정보 수집"),
    MARKETING("TR002", "마케팅 수신")

    ;

    private final String code;
    private final String description;
}
