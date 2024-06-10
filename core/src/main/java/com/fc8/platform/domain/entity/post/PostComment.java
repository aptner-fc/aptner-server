package com.fc8.platform.domain.entity.post;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @JoinColumn(name = "admin_id", columnDefinition = "bigint unsigned comment '어드민 ID'")
    private Admin admin;

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

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostCommentImage> postCommentImages = new ArrayList<>();

    @Transient
    private boolean isBlocked;

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

    public void modify(String content) {
        if (Objects.equals(this.content, content)) {
            return;
        }

        this.content = content;
    }

    public void delete() {
        if (this.deletedAt != null) {
            throw new InvalidParamException(ErrorCode.ALREADY_DELETED_POST_COMMENT);
        }

        this.deletedAt = LocalDateTime.now();
    }

    public void changeBlockStatus(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}
