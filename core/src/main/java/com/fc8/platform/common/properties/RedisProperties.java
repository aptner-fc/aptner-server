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

    public static final String VERIFICATION_CODE_PREFIX = "sms:verification::";

    public static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static final long ATTEMPT_MAX_COUNT = 5;

    public static final String ATTEMPT_COUNT_SUFFIX = ":attempts";

}
