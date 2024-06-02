package com.fc8.platform.domain.entity.post;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "post_file"
)
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '파일 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", columnDefinition = "bigint unsigned comment '소통 게시판 ID'")
    private Post post;

    @Column(name = "file_name", nullable = false, columnDefinition = "varchar(255) comment '파일 이름'")
    private String name;

    @Column(name = "file_path", nullable = false, columnDefinition = "varchar(255) comment '파일 경로'")
    private String path;

    @Column(name = "file_extension", nullable = false, columnDefinition = "varchar(20) comment '파일 확장자'")
    private String fileExtension;

    @Column(name = "file_size", nullable = false, columnDefinition = "bigint unsigned comment '파일 크기'")
    private Long size;

    public static PostFile create(Post post, String originalFileName, String fileUrl, String fileExtension, Long fileSize) {
        return PostFile.builder()
                .post(post)
                .name(originalFileName)
                .path(fileUrl)
                .fileExtension(fileExtension)
                .size(fileSize)
                .build();
    }

}