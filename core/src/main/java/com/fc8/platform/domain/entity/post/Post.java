package com.fc8.platform.domain.entity.post;

import com.fc8.platform.domain.BaseApartEntity;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
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

    @ManyToOne
    @JoinColumn(name = "member_id", columnDefinition = "bigint unsigned comment '회원 ID'")
    private Member member;

    @Column(name = "image_path", columnDefinition = "varchar(255) comment '썸네일 경로'")
    private String imagePath;

}
