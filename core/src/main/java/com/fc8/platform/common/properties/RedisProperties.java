package com.fc8.platform.common.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class RedisProperties {

    public static final int VERIFICATION_CODE_LENGTH = 6;
    public static final long VERIFICATION_CODE_EXPIRATION = 3;
    public static final String VERIFICATION_CODE_PREFIX = "sms:verification::";

}
