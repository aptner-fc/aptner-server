package com.fc8.platform.dto.notification;

import com.fc8.platform.dto.notification.web.QnaAnswerWebPushInfo;
import lombok.Builder;

@Builder
public record QnaNotification(
        QnaAnswerWebPushInfo qnaAnswerWebPushInfo
) {
    public static QnaNotification onlyWebPush(QnaAnswerWebPushInfo qnaAnswerWebPushInfo) {
        return QnaNotification.builder()
                .qnaAnswerWebPushInfo(qnaAnswerWebPushInfo)
                .build();
    }
}
