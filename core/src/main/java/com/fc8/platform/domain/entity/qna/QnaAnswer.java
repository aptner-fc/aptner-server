package com.fc8.platform.domain.entity.qna;

import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.entity.admin.Admin;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "qna_answer"
)
public class QnaAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '댓글 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "qna_id", columnDefinition = "bigint unsigned comment '민원 게시판 ID'")
    private Qna qna;

    @ManyToOne
    @JoinColumn(name = "admin_id", columnDefinition = "bigint unsigned comment '어드민 ID'")
    private Admin admin;

    @Column(name = "content", columnDefinition = "varchar(255) comment '답변 내용'")
    private String content;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static QnaAnswer createAnswer(Qna qna, Admin admin, String content) {
        return QnaAnswer.builder()
            .qna(qna)
            .admin(admin)
            .content(content)
            .build();
    }
}
