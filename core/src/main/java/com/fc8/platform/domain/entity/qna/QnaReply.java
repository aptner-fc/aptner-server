package com.fc8.platform.domain.entity.qna;

import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "qna_reply"
)
public class QnaReply extends BaseTimeEntity {

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
    @JoinColumn(name = "qna_reply_id", columnDefinition = "bigint unsigned comment '상위 댓글 ID'")
    private QnaReply parent;

    @Column(name = "content", columnDefinition = "varchar(255) comment '댓글 내용'")
    private String content;

}
