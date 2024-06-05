package com.fc8.platform.domain.entity.member;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "member_block"
)
public class MemberBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '차단한 회원 ID'")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "blocked", columnDefinition = "bigint unsigned comment '차단된 회원 ID'")
    private Member blocked;

    @Column(name = "blocked_at", columnDefinition = "datetime comment '차단 일시'")
    private LocalDateTime blockedAt;

    public static MemberBlock block(Member member, Member blocked) {
        return MemberBlock.builder()
                .member(member)
                .blocked(blocked)
                .blockedAt(LocalDateTime.now())
                .build();
    }
}
