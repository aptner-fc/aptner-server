package com.fc8.platform.domain.entity.pinned;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "pinned_post_file"
)
public class PinnedPostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '파일 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pinned_post_id", columnDefinition = "bigint unsigned comment '게시판 ID'")
    private PinnedPost pinnedPost;

    @Column(name = "file_name", nullable = false, columnDefinition = "varchar(255) comment '파일 이름'")
    private String name;

    @Column(name = "file_path", nullable = false, columnDefinition = "varchar(255) comment '파일 경로'")
    private String path;

    @Column(name = "file_extension", nullable = false, columnDefinition = "varchar(20) comment '파일 확장자'")
    private String fileExtension;

    @Column(name = "file_size", nullable = false, columnDefinition = "bigint unsigned comment '파일 크기'")
    private Long size;

    public static PinnedPostFile create(PinnedPost pinnedPost, String originalFileName, String fileUrl, String fileExtension, Long fileSize) {
        return PinnedPostFile.builder()
                .pinnedPost(pinnedPost)
                .name(originalFileName)
                .path(fileUrl)
                .fileExtension(fileExtension)
                .size(fileSize)
                .build();
    }

}