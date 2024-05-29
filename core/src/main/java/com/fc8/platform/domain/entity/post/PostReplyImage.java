package com.fc8.platform.domain.entity.post;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "post_reply_image"
)
public class PostReplyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '댓글 이미지 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_reply_id", columnDefinition = "bigint unsigned comment '소통 게시판 댓글 ID'")
    private PostComment postComment;

    @Column(name = "seq", columnDefinition = "int comment '이미지 순서'")
    private Integer seq;

    @Column(name = "image_path", columnDefinition = "varchar(255) comment '썸네일 경로'")
    private String imagePath;

}
