package com.fc8.platform.dto.notification.web;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.enums.MessageType;
import com.fc8.platform.domain.enums.NotificationType;
import com.fc8.platform.dto.record.QnaInfo;
import lombok.Builder;

@Builder
public record QnaAnswerWebPushInfo(
        Long id,
        Long memberId,
        String title,
        String content,
        NotificationType notificationType,
        MessageType messageType,
        QnaInfo qna
) {
    public static QnaAnswerWebPushInfo fromEntity(Long id,
                                                  String title,
                                                  String content,
                                                  NotificationType notificationType,
                                                  MessageType messageType,
                                                  Qna qna) {

        Member member = qna.getMember();
        return QnaAnswerWebPushInfo.builder()
                .id(id)
                .memberId(member.getId())
                .title(title)
                .content(content)
                .notificationType(notificationType)
                .messageType(messageType)
                .qna(QnaInfo.fromEntity(qna, member, qna.getCategory()))
                .build();
    }
}
