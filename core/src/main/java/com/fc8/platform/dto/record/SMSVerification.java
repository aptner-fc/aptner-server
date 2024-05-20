package com.fc8.platform.dto.record;

import java.time.LocalDateTime;

public record SMSVerification(String verificationCode,
                              LocalDateTime expiredAt) {
}
