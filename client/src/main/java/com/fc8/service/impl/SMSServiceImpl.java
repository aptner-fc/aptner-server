package com.fc8.service.impl;

import com.fc8.platform.common.utils.GenerateUtils;
import com.fc8.platform.common.utils.RedisUtils;
import com.fc8.platform.common.utils.SMSUtils;
import com.fc8.platform.dto.record.SMSVerificationCode;
import com.fc8.platform.dto.record.SMSVerificationInfo;
import com.fc8.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMSServiceImpl implements SMSService {

    private final SMSUtils smsUtils;
    private final RedisUtils redisUtils;

    @Override
    @Transactional
    public SMSVerificationCode sendVerificationCode(String phone) {
        final String verificationCode = GenerateUtils.generateVerificationCode();
        final LocalDateTime now = LocalDateTime.now();

        smsUtils.sendVerificationMessage(phone, verificationCode);
        redisUtils.storeVerificationCode(phone, verificationCode);
        return new SMSVerificationCode(verificationCode, now.plusMinutes(3));
    }

    @Override
    @Transactional
    public SMSVerificationInfo verifyCode(String phone, String verificationCode) {
        final boolean isVerified = redisUtils.isValidateAndVerified(phone, verificationCode);
        final Long attempts = redisUtils.getAttemptCount(phone);
        final LocalDateTime now = LocalDateTime.now();

        return isVerified ?
                SMSVerificationInfo.verify(verificationCode, attempts, now) : SMSVerificationInfo.fail(verificationCode, attempts);
    }
}
