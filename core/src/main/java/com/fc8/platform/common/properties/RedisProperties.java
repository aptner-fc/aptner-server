package com.fc8.platform.common.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class RedisProperties {

    public static final int VERIFICATION_CODE_LENGTH = 6;

    // SMS 인증 발송 시점
    public static final long VERIFICATION_CODE_EXPIRATION = 3;

    // SMS 인증 완료 시점
    public static final long VALIDATION_CODE_EXPIRATION = 10;

    public static final long ATTEMPT_MAX_COUNT = 5;

    public static final String VERIFICATION_CODE_PREFIX = "sms:verification::";

    public static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static final String ATTEMPT_COUNT_SUFFIX = ":attempts";

    public static final int TIME_TO_UPDATE_VIEW_COUNT = 5;
    public static final String VIEW_ASTERISK = "*";
    public static final String VIEW_POST = "VIEW:POST";
    public static final String VIEW_QNA = "VIEW:QNA";
    public static final String VIEW_NOTICE = "VIEW:NOTICE";
    public static final String VIEW_PINNED_POST = "VIEW:PINNED_POST";
    public static final String VIEW_DISCLOSURE = "VIEW:DISCLOSURE";

}
