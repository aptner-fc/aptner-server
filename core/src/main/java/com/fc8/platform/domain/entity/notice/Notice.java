package com.fc8.platform.domain.entity.notice;

import com.fc8.platform.domain.BaseApartEntity;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "notice"
)
public class Notice extends BaseApartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '공지사항 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", columnDefinition = "bigint unsigned comment '카테고리 ID'")
    private Category category;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(20) comment '제목'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "mediumtext comment '내용'")
    private String content;

    @ManyToOne
    @JoinColumn(name = "admin_id", columnDefinition = "bigint unsigned comment '어드민 회원 ID'")
    private Admin admin;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

}
