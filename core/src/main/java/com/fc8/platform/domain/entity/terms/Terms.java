package com.fc8.platform.domain.entity.terms;


import com.fc8.platform.converter.TermsTypeConverter;
import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.enums.TermsType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "terms"
)
public class Terms extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '약관 고유 번호'")
//    @Column(name = "id", columnDefinition = "BIGINT AUTO_INCREMENT comment '회원 고유 번호'")
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(20) comment '약관명'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "mediumtext comment '약관 내용'")
    private String content;

    @Convert(converter = TermsTypeConverter.class)
    @Column(name = "type", nullable = false, updatable = false, columnDefinition = "varchar(20) comment '약관 타입'")
    private TermsType type;

    @Column(name = "is_used", columnDefinition = "tinyint comment '사용 여부'")
    private boolean isUsed;

    @Column(name = "is_required", columnDefinition = "tinyint comment '필수 여부'")
    private boolean isRequired;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public static Terms create(String title,
                               String content,
                               TermsType type,
                               boolean isUsed,
                               boolean isRequired) {
        return Terms.builder()
                .title(title)
                .content(content)
                .type(type)
                .isUsed(isUsed)
                .isRequired(isRequired)
                .build();
    }

}
