package com.fc8.platform.domain.entity.post;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.BaseApartEntity;
import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
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
        name = "post"
)
public class Post extends BaseApartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '소통 게시판 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", columnDefinition = "bigint unsigned comment '카테고리 ID'")
    private Category category;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(20) comment '제목'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "mediumtext comment '내용'")
    private String content;

    // 게시글 상태 추가 TODO

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @Column(name = "thumbnail_path", columnDefinition = "varchar(255) comment '썸네일 경로'")
    private String thumbnailPath;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    @Builder.Default
    @Column(name = "view_count", columnDefinition = "bigint unsigned comment '조회수'")
    private Long viewCount = 0L;

    public static Post create(Category category,
                              Member member,
                              Apart apart,
                              String title,
                              String content) {
        Post post = Post.builder()
                .category(category)
                .member(member)
                .title(title)
                .content(content)
                .build();
        post.updateApart(apart);

        return post;
    }

    public void updateThumbnail(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
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
