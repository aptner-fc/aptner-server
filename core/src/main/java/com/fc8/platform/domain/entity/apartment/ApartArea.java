package com.fc8.platform.domain.entity.apartment;

import com.fc8.platform.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "apart_area"
)
public class ApartArea extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apart_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '아파트 정보'")
    private Apart apart;

    @Column(name = "area", columnDefinition = "int comment '소통 게시판 고유 번호'")
    private Integer area;

    @Column(name = "is_used", columnDefinition = "tinyint comment '사용 여부'")
    private boolean isUsed;

    @Column(name = "image_path", columnDefinition = "varchar(255) comment '이미지 경로'")
    private String imagePath;

}
