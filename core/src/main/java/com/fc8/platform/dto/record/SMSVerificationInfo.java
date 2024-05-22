package com.fc8.platform.dto.record;

import java.time.LocalDateTime;

public record SMSVerificationInfo(boolean isVerify,
                                  String verificationCode,
                                  Long attempt,
                                  LocalDateTime expiredAt) {

    public static SMSVerificationInfo verify(String verificationCode, Long attempt, LocalDateTime expiredAt) {
        return new SMSVerificationInfo(true, verificationCode, attempt, expiredAt);
    }

    public static SMSVerificationInfo fail(String verificationCode, Long attempt) {
        return new SMSVerificationInfo(false, verificationCode, attempt, null);
    }
}
