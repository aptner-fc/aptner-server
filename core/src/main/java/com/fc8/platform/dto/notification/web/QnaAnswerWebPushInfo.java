package com.fc8.platform.dto.notification.web;

import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.enums.MessageType;
import com.fc8.platform.domain.enums.NotificationType;
import com.fc8.platform.dto.record.QnaInfo;
import lombok.Builder;

@Builder
public record QnaAnswerWebPushInfo(
        Long memberId,
        String title,
        String content,
        NotificationType notificationType,
        MessageType messageType,
        QnaInfo qna
) {
    public static QnaAnswerWebPushInfo fromQnaEntity(String title,
                                                     String content,
                                                     Qna qna) {

        return QnaAnswerWebPushInfo.builder()
                .memberId(qna.getMember().getId())
                .title(title)
                .content(content)
                .notificationType(NotificationType.QNA)
                .messageType(MessageType.INFO)
                .qna(QnaInfo.fromEntity(qna, qna.getMember(), qna.getCategory()))
                .build();
    }
}
