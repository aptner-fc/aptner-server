package com.fc8.platform.domain.entity.mapping;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.notification.Notification;
import com.fc8.platform.domain.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "notification_member_mapping"
)
public class NotificationMemberMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '매핑 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '알림 고유 번호'")
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20) comment '알림 상태'")
    private NotificationStatus status;

    @Column(name = "read_at", columnDefinition = "datetime comment '읽음 일시'")
    private LocalDateTime readAt;

    public static NotificationMemberMapping create(Notification notification,
                                                   Member member) {
        return NotificationMemberMapping.builder()
                .notification(notification)
                .member(member)
                .status(NotificationStatus.UNREAD)
                .build();
    }
}
