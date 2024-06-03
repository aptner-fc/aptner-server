package com.fc8.platform.domain.entity.notice;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "notice_file"
)
public class NoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '파일 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "qna_id", columnDefinition = "bigint unsigned comment '공지사항 ID'")
    private Notice notice;

    @Column(name = "file_name", nullable = false, columnDefinition = "varchar(255) comment '파일 이름'")
    private String name;

    @Column(name = "file_path", nullable = false, columnDefinition = "varchar(255) comment '파일 경로'")
    private String path;

    @Column(name = "file_size", nullable = false, columnDefinition = "bigint unsigned comment '파일 크기'")
    private Long size;

}
