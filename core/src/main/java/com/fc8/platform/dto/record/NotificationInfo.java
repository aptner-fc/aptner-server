package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.notification.Notification;
import com.fc8.platform.domain.enums.MessageType;
import com.fc8.platform.domain.enums.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationInfo(
        Long id,
        String title,
        String content,
        NotificationType notificationType,
        MessageType messageType,
        LocalDateTime createdAt
) {
    public static NotificationInfo fromEntity(Notification notification) {
        return NotificationInfo.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .notificationType(notification.getNotificationType())
                .messageType(notification.getMessageType())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
