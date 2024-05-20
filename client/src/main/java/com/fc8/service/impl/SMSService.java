package com.fc8.service.impl;

import com.fc8.platform.dto.record.SMSVerification;

public interface SMSService {

    SMSVerification sendVerificationCode(String phone);
}
