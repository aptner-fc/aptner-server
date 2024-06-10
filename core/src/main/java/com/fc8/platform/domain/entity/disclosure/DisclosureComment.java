package com.fc8.platform.domain.entity.disclosure;

import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "disclosure_comment"
)
public class DisclosureComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '댓글 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "disclosure_id", columnDefinition = "bigint unsigned comment '의무공개 ID'")
    private Disclosure disclosure;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "parent_id", columnDefinition = "bigint unsigned comment '상위 댓글 ID'")
    private DisclosureComment parent;

    @Column(name = "content", columnDefinition = "varchar(255) comment '댓글 내용'")
    private String content;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static DisclosureComment createComment(Disclosure disclosure, Member member, String content) {
        return DisclosureComment.builder()
            .disclosure(disclosure)
            .member(member)
            .content(content)
            .build();
    }

    public static DisclosureComment createReply(Disclosure disclosure, DisclosureComment comment, Member member, String content) {
        return DisclosureComment.builder()
            .disclosure(disclosure)
            .parent(comment)
            .member(member)
            .content(content)
            .build();
    }
}
