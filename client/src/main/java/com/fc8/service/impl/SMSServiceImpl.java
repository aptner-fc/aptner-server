package com.fc8.service.impl;

import com.fc8.platform.common.utils.GenerateUtils;
import com.fc8.platform.common.utils.RedisUtils;
import com.fc8.platform.common.utils.SMSUtils;
import com.fc8.platform.dto.record.SMSVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMSServiceImpl implements SMSService {

    private final SMSUtils smsUtils;
    private final RedisUtils redisUtils;

    @Override
    public SMSVerification sendVerificationCode(String phone) {
        String verificationCode = GenerateUtils.generateVerificationCode();
        LocalDateTime now = LocalDateTime.now();

        smsUtils.sendVerificationMessage(phone, verificationCode);
        redisUtils.storeVerificationCode(phone, verificationCode);
        return new SMSVerification(verificationCode, now.plusMinutes(3));
    }
}
