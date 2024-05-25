package com.fc8.platform.domain.entity.post;

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
        name = "post_comment"
)
public class PostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '댓글 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", columnDefinition = "bigint unsigned comment '소통 게시판 ID'")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "parent_id", columnDefinition = "bigint unsigned comment '상위 댓글 ID'")
    private PostComment parent;

    @Column(name = "content", columnDefinition = "varchar(255) comment '댓글 내용'")
    private String content;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static PostComment createComment(Post post,
                                            Member member,
                                            String content) {
        return PostComment.builder()
                .post(post)
                .member(member)
                .content(content)
                .build();
    }

    public static PostComment createReply(Post post,
                                          PostComment comment,
                                          Member member,
                                          String content) {
        return PostComment.builder()
                .post(post)
                .parent(comment)
                .member(member)
                .content(content)
                .build();
    }

}
