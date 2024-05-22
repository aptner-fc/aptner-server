package com.fc8.service;

import com.fc8.platform.dto.record.SMSVerificationCode;
import com.fc8.platform.dto.record.SMSVerificationInfo;

public interface SMSService {

    SMSVerificationCode sendVerificationCode(String phone);

    SMSVerificationInfo verifyCode(String phone, String verificationCode);
}
