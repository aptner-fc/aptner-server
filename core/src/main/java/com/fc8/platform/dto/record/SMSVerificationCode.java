package com.fc8.platform.dto.record;

import java.time.LocalDateTime;

public record SMSVerificationCode(String verificationCode,
                                  LocalDateTime expiredAt) {
}
