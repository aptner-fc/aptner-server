package com.fc8.platform.domain.entity.mapping;


import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.terms.Terms;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "terms_member_mapping"
)
public class TermsMemberMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '약관 고유 번호'")
//    @Column(name = "id", columnDefinition = "BIGINT AUTO_INCREMENT comment '회원 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '약관 고유 번호'")
    private Terms terms;

    @Column(name = "agreed_at", columnDefinition = "datetime comment '동의 일시'")
    private LocalDateTime agreedAt;

    public static TermsMemberMapping create(Member member, Terms terms, boolean isAgreed, LocalDateTime agreedAt) {
        return TermsMemberMapping.builder()
                .member(member)
                .terms(terms)
                .agreedAt(isAgreed ? agreedAt : null)
                .build();
    }
}
