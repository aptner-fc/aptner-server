package com.fc8.platform.domain.entity.admin;

import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "admin"
//        uniqueConstraints = {
//                @UniqueConstraint(name = "UK_email", columnNames = "email")
//        }
)
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '어드민 고유 번호'")
//    @Column(name = "id", columnDefinition = "BIGINT AUTO_INCREMENT comment '회원 고유 번호'")
    private Long id;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(255) comment '이메일'")
    private String email;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(45) comment '회원 이름'")
    private String name;

    @Column(name = "nickname", nullable = false, columnDefinition = "varchar(45) comment '닉네임'")
    private String nickname;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(255) comment '비밀번호'")
    private String password;

    @Column(name = "phone", nullable = false, columnDefinition = "varchar(20) comment '전화번호'")
    private String phone;

    @Column(name = "profile_image", columnDefinition = "varchar(255) comment '프로필 이미지'")
    private String profileImage;

    @Column(name = "introduce", columnDefinition = "varchar(255) comment '자기 소개'")
    private String introduce;

    @Column(name = "gender", nullable = false, columnDefinition = "varchar(20) comment '성별'")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apart_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '관리 아파트 고유 번호(권한)'")
    private Apart apart;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '탈퇴 일시'")
    private LocalDateTime deletedAt;

    public static Admin create(String email,
                               String name,
                               String nickname,
                               String password,
                               String phone,
                               Gender gender,
                               Apart apart) {
        return Admin.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(password)
                .phone(phone)
                .gender(gender)
                .apart(apart)
                .build();
    }

    public void updateApart(Apart apart) {
        if (this.apart == apart) {
            return;
        }

        this.apart = apart;
    }
}
