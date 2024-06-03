package com.fc8.platform.domain.entity.notice;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.enums.EmojiType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "notice_emoji"
)
public class NoticeEmoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '이모지 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notice_id", columnDefinition = "bigint unsigned comment '공지사항 ID'")
    private Notice notice;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "emoji", nullable = false, columnDefinition = "varchar(20) comment '이모지'")
    private EmojiType emoji;

}
