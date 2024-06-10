package com.fc8.platform.domain.entity.notice;

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
    name = "notice_comment"
)
public class NoticeComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '댓글 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notice_id", columnDefinition = "bigint unsigned comment '공지사항 ID'")
    private Notice notice;

    @ManyToOne
    @JoinColumn(name = "admin_id", columnDefinition = "bigint unsigned comment '어드민 ID'")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "parent_id", columnDefinition = "bigint unsigned comment '상위 댓글 ID'")
    private NoticeComment parent;

    @Column(name = "content", columnDefinition = "varchar(255) comment '댓글 내용'")
    private String content;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "noticeComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NoticeCommentImage> noticeCommentImages = new ArrayList<>();

    @Transient
    private boolean isBlocked;

    public static NoticeComment createComment(Notice notice, Member member, String content) {
        return NoticeComment.builder()
            .notice(notice)
            .member(member)
            .content(content)
            .build();
    }

    public static NoticeComment createReply(Notice notice, NoticeComment comment, Member member, String content) {
        return NoticeComment.builder()
            .notice(notice)
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
