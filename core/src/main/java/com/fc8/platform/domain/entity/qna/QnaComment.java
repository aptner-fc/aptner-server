package com.fc8.platform.domain.entity.qna;

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
    name = "qna_comment"
)
public class QnaComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '댓글 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "qna_id", columnDefinition = "bigint unsigned comment '민원 게시판 ID'")
    private Qna qna;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "parent_id", columnDefinition = "bigint unsigned comment '상위 댓글 ID'")
    private QnaComment parent;

    @Column(name = "content", columnDefinition = "varchar(255) comment '댓글 내용'")
    private String content;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static QnaComment createComment(Qna qna, Member member, String content) {
        return QnaComment.builder()
            .qna(qna)
            .member(member)
            .content(content)
            .build();
    }

    public static QnaComment createReply(Qna qna, QnaComment comment, Member member, String content) {
        return QnaComment.builder()
            .qna(qna)
            .parent(comment)
            .member(member)
            .content(content)
            .build();
    }

}
