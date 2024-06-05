package com.fc8.platform.domain.entity.notification;

import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "notification"
)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '알림 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @Column(name = "content", nullable = false, columnDefinition = "mediumtext comment '내용'")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20) comment '알림 상태'")
    private NotificationStatus status;

}
