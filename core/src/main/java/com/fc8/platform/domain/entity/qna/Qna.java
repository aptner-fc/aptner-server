package com.fc8.platform.domain.entity.qna;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.BaseApartEntity;
import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.enums.ProcessingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "qna"
)
public class Qna extends BaseApartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '민원 게시판 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", columnDefinition = "bigint unsigned comment '카테고리 ID'")
    private Category category;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(20) comment '제목'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "mediumtext comment '내용'")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", nullable = false, columnDefinition = "varchar(20) comment '처리 상태'")
    private ProcessingStatus processingStatus;

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    @Column(name = "is_private", nullable = false, columnDefinition = "tinyint(1) comment '비밀글 여부'")
    private boolean isPrivate;


    public static Qna create(
        Category category, Member member, Apart apart, String title, String content, boolean isPrivate
    ) {
        Qna qna = Qna.builder()
            .category(category)
            .member(member)
            .title(title)
            .content(content)
            .isPrivate(isPrivate)
            .processingStatus(ProcessingStatus.접수)
            .build();
        qna.updateApart(apart);

        return qna;
    }

    public void delete() {
        if (this.deletedAt != null) {
            throw new InvalidParamException(ErrorCode.ALREADY_DELETED_POST);
        }

        this.deletedAt = LocalDateTime.now();
    }

    public void changeCategory(Category category) {
        if (this.category == category) {
            return;
        }

        this.category = category;
    }

    public void modify(String title, String content) {
        if (Objects.equals(this.title, title) && Objects.equals(this.content, content)) {
            return;
        }

        this.title = title;
        this.content = content;
    }

}
