package com.fc8.platform.domain.entity.disclosure;

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
    name = "disclosure_emoji"
)
public class DisclosureEmoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '이모지 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "disclosure_id", columnDefinition = "bigint unsigned comment '의무공개 ID'")
    private Disclosure disclosure;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "emoji", nullable = false, columnDefinition = "varchar(20) comment '이모지'")
    private EmojiType emoji;

    public static DisclosureEmoji create(Disclosure disclosure, Member member, EmojiType emoji) {
        return DisclosureEmoji.builder()
            .disclosure(disclosure)
            .member(member)
            .emoji(emoji)
            .build();
    }

}
