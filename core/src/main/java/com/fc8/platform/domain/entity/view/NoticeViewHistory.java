package com.fc8.platform.domain.entity.view;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.notice.Notice;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "notice_view_history"
)
public class NoticeViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, referencedColumnName = "id", columnDefinition = "bigint comment '연결된 회원 ID'")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "notice_id", columnDefinition = "bigint unsigned comment '공지사항 게시판 ID'")
    private Notice notice;

    @Builder.Default
    @Column(name = "created_at", updatable = false, columnDefinition = "datetime comment '생성일'")
    private LocalDateTime createdAt = LocalDateTime.now();
}
