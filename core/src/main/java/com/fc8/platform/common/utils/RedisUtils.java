package com.fc8.platform.common.utils;

import com.fc8.platform.common.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public void storeVerificationCode(String phone, String verificationCode) {
        String key = RedisProperties.VERIFICATION_CODE_PREFIX + phone;
        redisTemplate.opsForValue().set(key, verificationCode, RedisProperties.VERIFICATION_CODE_EXPIRATION, TimeUnit.MINUTES);
    }

}
