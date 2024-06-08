package com.fc8.platform.domain.entity.pinned;

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
        name = "pinned_post_emoji"
)
public class PinnedPostEmoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '이모지 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pinned_post_id", columnDefinition = "bigint unsigned comment '게시판 ID'")
    private PinnedPost pinnedPost;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "emoji", nullable = false, columnDefinition = "varchar(20) comment '이모지'")
    private EmojiType emoji;

    public static PinnedPostEmoji create(PinnedPost pinnedPost,
                                         Member member,
                                         EmojiType emoji) {
        return PinnedPostEmoji.builder()
                .pinnedPost(pinnedPost)
                .member(member)
                .emoji(emoji)
                .build();
    }

}
