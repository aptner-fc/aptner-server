package com.fc8.platform.domain.entity.mapping;

import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.embedded.ApartDetail;
import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "apart_member_mapping"
)
public class ApartMemberMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '약관 고유 번호'")
//    @Column(name = "id", columnDefinition = "BIGINT AUTO_INCREMENT comment '회원 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apart_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '아파트 고유 번호'")
    private Apart apart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @Column(name = "is_main", columnDefinition = "tinyint comment '메인 여부'")
    private boolean isMain;

    @Embedded
    private ApartDetail apartDetail;

    public static ApartMemberMapping create(Apart apart,
                                            Member member,
                                            boolean isMain,
                                            ApartDetail apartDetail) {
        return ApartMemberMapping.builder()
                .apart(apart)
                .member(member)
                .isMain(isMain)
                .apartDetail(apartDetail)
                .build();
    }

    public static ApartMemberMapping createFirst(Apart apart,
                                            Member member,
                                            ApartDetail apartDetail) {
        return ApartMemberMapping.builder()
                .apart(apart)
                .member(member)
                .isMain(true)
                .apartDetail(apartDetail)
                .build();
    }
}
