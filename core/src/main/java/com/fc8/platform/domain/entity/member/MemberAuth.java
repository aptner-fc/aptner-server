package com.fc8.platform.domain.entity.member;

import com.fc8.platform.domain.entity.admin.Admin;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "member_auth"
)
public class MemberAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '인증 어드민'")
    private Admin admin;

    @Column(name = "authenticated_at", columnDefinition = "datetime comment '인증 일시'")
    private LocalDateTime authenticatedAt;

    public static MemberAuth of(Member member, Admin admin) {
        return MemberAuth.builder()
                .member(member)
                .admin(admin)
                .authenticatedAt(LocalDateTime.now())
                .build();
    }

}
