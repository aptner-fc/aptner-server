package com.fc8.platform.domain.entity.disclosure;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "disclosure_file"
)
public class DisclosureFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint unsigned comment '파일 고유 번호'")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "disclosure_id", columnDefinition = "bigint unsigned comment '의무공개 ID'")
    private Disclosure disclosure;

    @Column(name = "file_name", nullable = false, columnDefinition = "varchar(255) comment '파일 이름'")
    private String name;

    @Column(name = "file_path", nullable = false, columnDefinition = "varchar(255) comment '파일 경로'")
    private String path;

    @Column(name = "file_size", nullable = false, columnDefinition = "bigint unsigned comment '파일 크기'")
    private Long size;

}
