package com.fc8.platform.domain.entity.apartment;

import com.fc8.platform.converter.ApartTypeConverter;
import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.enums.ApartType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "apart"
//        uniqueConstraints = {
//                @UniqueConstraint(name = "UK_email", columnNames = "email")
//        }
)
public class Apart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '아파트 고유 번호'")
//    @Column(name = "id", columnDefinition = "BIGINT AUTO_INCREMENT comment '회원 고유 번호'")
    private Long id;

    @Column(name = "code", nullable = false, columnDefinition = "varchar(20) comment '아파트 코드'")
    private String code;

    @Convert(converter = ApartTypeConverter.class)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(20) comment '아파트 타입'")
    private ApartType type;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(20) comment '아파트명(표기)'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "mediumtext comment '아파트 소개'")
    private String content;

    @Column(name = "address", nullable = false, columnDefinition = "mediumtext comment '주소'")
    private String address;

    @Column(name = "is_used", columnDefinition = "tinyint comment '사용 여부'")
    private boolean isUsed;
}
