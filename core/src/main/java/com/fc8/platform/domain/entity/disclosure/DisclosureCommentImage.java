package com.fc8.platform.domain.entity.disclosure;

import com.fc8.platform.domain.entity.notice.NoticeComment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "disclosure_comment_image"
)
public class DisclosureCommentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '댓글 이미지 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "disclosure_comment_id", columnDefinition = "bigint unsigned comment '의무공개 댓글 ID'")
    private NoticeComment noticeComment;

    @Column(name = "seq", columnDefinition = "int comment '이미지 순서'")
    private int seq;

    @Column(name = "image_path", columnDefinition = "varchar(255) comment '썸네일 경로'")
    private String imagePath;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

}
