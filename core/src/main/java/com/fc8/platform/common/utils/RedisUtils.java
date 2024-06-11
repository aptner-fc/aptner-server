package com.fc8.platform.common.utils;

import com.fc8.platform.common.exception.CustomRedisException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.properties.RedisProperties;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public String getViewCountValueByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }

    public void storeVerificationCode(String phone, String verificationCode) {
        // 1. 휴대폰 번호로 키 생성
        String key = getVerificationKeyByPhone(phone);

        // 2. 인메모리 DB에 KEY-VALUE 저장
        redisTemplate.opsForValue().set(key, verificationCode, RedisProperties.VERIFICATION_CODE_EXPIRATION, TimeUnit.MINUTES);

        // 3. 시도 횟수 초기화
        resetAttemptCount(key);
    }

    public boolean isValidateAndVerified(String phone, String inputCode) {
        // 1. 휴대폰 번호로 키 생성
        String key = getVerificationKeyByPhone(phone);

        // 2. 인메모리 DB에서 인증 코드 조회
        Optional<String> verificationCode = Optional.ofNullable(redisTemplate.opsForValue().get(key));

        // 3. 유효성 검사, 인증 성공시 10분의 키 유효성 부여하며 실패시 시도 횟수 증가(인증 만료시 예외 출력)
        return validateVerificationCode(key, inputCode, verificationCode);
    }

    private boolean validateVerificationCode(String key, String inputCode, Optional<String> verificationCode) {
        return verificationCode.map(code -> {
            if (code.equals(inputCode)) {
                redisTemplate.expire(key, RedisProperties.VALIDATION_CODE_EXPIRATION, TimeUnit.MINUTES);
                return true;
            } else {
                handleFailedAttempt(key);
                return false;
            }
        }).orElseThrow(() -> new CustomRedisException(ErrorCode.INVALID_OR_EXPIRED_SMS));
    }

    private void resetAttemptCount(String key) {
        // 1. 시도 횟수 초기화
        String attemptKey = getAttemptKey(key);

        // 2. 인메모리 DB 데이터 삭제
        redisTemplate.delete(attemptKey);
    }

    private String getAttemptKey(@NotNull(message = "키가 누락되었습니다.") String key) {
        return key + RedisProperties.ATTEMPT_COUNT_SUFFIX;
    }

    public Long getAttemptCount(String phone) {
        String key = getVerificationKeyByPhone(phone);
        String attemptKey = getAttemptKey(key);
        return Optional.ofNullable(redisTemplate.opsForValue().get(attemptKey))
                .map(Long::parseLong)
                .orElse(0L);
    }

    private String getVerificationKeyByPhone(@NotNull(message = "전화번호가 누락되었습니다.") String phone) {
        return RedisProperties.VERIFICATION_CODE_PREFIX + phone;
    }

    private void handleFailedAttempt(String key) {
        // 1. 시도 횟수 초과시 인증 실패
        deleteIfExceededMaxAttempt(key);
    }

    private void deleteIfExceededMaxAttempt(String key) {
        // 1. 시도 횟수 조회
        String attemptKey = getAttemptKey(key);
        Optional<Long> attempts = Optional.ofNullable(redisTemplate.opsForValue().increment(attemptKey));

        // 2. 시도 횟수가 5회 이상일 때 인증 초기화
        if (attempts.isPresent() && attempts.get() >= RedisProperties.ATTEMPT_MAX_COUNT) {
            redisTemplate.delete(key);
            redisTemplate.delete(attemptKey);
            throw new CustomRedisException(ErrorCode.MAX_VERIFY_ATTEMPTS);
        }
    }

    public void increaseViewCountToRedis(String viewPrefix, Long id, Long viewCount) {
        String key = viewPrefix + "::" + id;
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        if (vop.get(key) == null) {
            vop.set(
                    key,
                    String.valueOf(viewCount + 1),
                    Duration.ofMinutes(RedisProperties.TIME_TO_UPDATE_VIEW_COUNT));
        } else {
            vop.increment(key);
        }
    }

}
