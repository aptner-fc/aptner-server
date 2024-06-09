package com.fc8.platform.domain.entity.notification;

import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.mapping.NotificationMemberMapping;
import com.fc8.platform.domain.enums.MessageType;
import com.fc8.platform.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "notification"
)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '알림 고유 번호'")
    private Long id;

    @Column(name = "title", columnDefinition = "varchar(150) comment '제목'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "varchar(500) comment '내용'")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(20) comment '알림 타입'")
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", columnDefinition = "varchar(20) comment '메세지 타입(안내, 광고 등)'")
    private MessageType messageType;

    @Column(name = "created_at", columnDefinition = "datetime comment '생성 일시'")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", columnDefinition = "BIGINT UNSIGNED comment '어드민 고유 번호'")
    private Admin admin;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationHistory> notificationHistories = new ArrayList<>();

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationMemberMapping> notificationMemberMappings = new ArrayList<>();

    public static Notification createBySystem(String title,
                                      String content,
                                      NotificationType notificationType,
                                      MessageType messageType) {
        return Notification.builder()
                .title(title)
                .content(content)
                .notificationType(notificationType)
                .messageType(messageType)
                .build();
    }

    public static Notification createByAdmin(String title,
                                      String content,
                                      NotificationType notificationType,
                                      MessageType messageType,
                                      Admin admin) {
        return Notification.builder()
                .title(title)
                .content(content)
                .notificationType(notificationType)
                .messageType(messageType)
                .admin(admin)
                .build();
    }

    public void addHistory(List<NotificationHistory> notificationHistories) {
        this.notificationHistories = notificationHistories;
    }

    public void addPushMemberMapping(List<NotificationMemberMapping> notificationMemberMappings) {
        this.notificationMemberMappings = notificationMemberMappings;
    }

}
