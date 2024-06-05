package com.fc8.platform.domain.entity.post;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "post_comment_image"
)
public class PostCommentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '댓글 이미지 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_comment_id", columnDefinition = "bigint unsigned comment '소통 게시판 댓글 ID'")
    private PostComment postComment;

    @Column(name = "seq", columnDefinition = "int comment '이미지 순서'")
    private int seq;

    @Column(name = "image_path", columnDefinition = "varchar(255) comment '썸네일 경로'")
    private String imagePath;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static PostCommentImage create(PostComment postComment, String imagePath) {
        return PostCommentImage.builder()
            .postComment(postComment)
            .imagePath(imagePath)
            .build();
    }

    public void delete() {
        if (this.deletedAt != null) {
            throw new InvalidParamException(ErrorCode.ALREADY_DELETED_POST_COMMENT_IMAGE);
        }

        this.deletedAt = LocalDateTime.now();
    }
}
