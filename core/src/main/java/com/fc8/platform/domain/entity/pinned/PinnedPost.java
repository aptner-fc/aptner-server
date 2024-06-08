package com.fc8.platform.domain.entity.pinned;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.BaseApartEntity;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.entity.category.Category;
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
        name = "pinned_post"
)
public class PinnedPost extends BaseApartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '중요글 게시판 고유 번호'")
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
    @JoinColumn(name = "admin_id", columnDefinition = "bigint unsigned comment '어드민 ID'")
    private Admin admin;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static PinnedPost create(Category category,
                                    Admin admin,
                                    Apart apart,
                                    String title,
                                    String content) {
        PinnedPost post = PinnedPost.builder()
                .category(category)
                .admin(admin)
                .title(title)
                .content(content)
                .build();
        post.updateApart(apart);

        return post;
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
