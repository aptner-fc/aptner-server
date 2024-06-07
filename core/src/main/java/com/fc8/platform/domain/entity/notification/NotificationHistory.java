package com.fc8.platform.domain.entity.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "notification_history"
)
public class NotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '알림 히스토리 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false, columnDefinition = "bigint unsigned comment '알림 고유 번호'")
    private Notification notification;

    @Column(name = "notification_at", columnDefinition = "datetime comment '알림 일시'")
    private LocalDateTime notificationAt;

    @Column(name = "notification_count", columnDefinition = "int comment '알림 개수'")
    private Integer notificationCount;

    @Column(name = "success_count", columnDefinition = "int comment '성공 개수'")
    private Integer successCount;

    @Column(name = "failure_count", columnDefinition = "int comment '실패 개수'")
    private Integer failureCount;

    public static NotificationHistory createWebPushHistory(Notification notification) {
        return NotificationHistory.builder()
                .notification(notification)
                .notificationAt(LocalDateTime.now())
                .notificationCount(1)
                .successCount(1)
                .failureCount(0)
                .build();
    }
}
