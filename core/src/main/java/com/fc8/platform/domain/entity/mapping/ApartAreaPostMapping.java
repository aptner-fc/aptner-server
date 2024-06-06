package com.fc8.platform.domain.entity.mapping;

import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.entity.apartment.ApartArea;
import com.fc8.platform.domain.entity.post.Post;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "apart_area_post_mapping"
)
public class ApartAreaPostMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '매핑 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apart_area_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '아파트 평수 고유 번호'")
    private ApartArea apartArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '소통 게시글 고유 번호'")
    private Post post;

    public static ApartAreaPostMapping create(ApartArea apartArea,
                                              Post post) {
        return ApartAreaPostMapping.builder()
                .apartArea(apartArea)
                .post(post)
                .build();
    }

    public void changeApartArea(ApartArea apartArea) {
        if (this.apartArea == apartArea) {
            return;
        }
        this.apartArea = apartArea;
    }

}
