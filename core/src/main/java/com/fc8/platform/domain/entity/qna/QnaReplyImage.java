package com.fc8.platform.domain.entity.qna;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "qna_reply_image"
)
public class QnaReplyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '댓글 이미지 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "qna_reply_id", columnDefinition = "bigint unsigned comment '민원 게시판 댓글 ID'")
    private QnaReply qnaReply;

    @Column(name = "image_path", columnDefinition = "varchar(255) comment '썸네일 경로'")
    private String imagePath;

}
