package com.fc8.platform.domain.entity.member;

import com.fc8.platform.domain.BaseTimeEntity;
import com.fc8.platform.domain.entity.mapping.ApartMemberMapping;
import com.fc8.platform.domain.enums.Gender;
import com.fc8.platform.domain.enums.MemberRole;
import com.fc8.platform.domain.enums.MemberStatus;
import com.fc8.platform.domain.enums.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "member"
//        uniqueConstraints = {
//                @UniqueConstraint(name = "UK_email", columnNames = "email")
//        }
)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
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

    @Column(name = "status", nullable = false, columnDefinition = "varchar(20) comment '회원 상태'")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column(name = "provider", nullable = false, columnDefinition = "varchar(20) comment '가입 정보'")
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar(20) comment '권한'")
    private MemberRole role = MemberRole.ROLE_USER;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '탈퇴 일시'")
    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApartMemberMapping> apartMemberMappings = new ArrayList<>();

    public static Member create(String email,
                                String name,
                                String nickname,
                                String encPassword,
                                String phone,
                                Gender gender) {
        return Member.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(encPassword)
                .phone(phone)
                .gender(gender)
                .status(MemberStatus.INACTIVE)
                .provider(Provider.APTNER)
                .role(MemberRole.ROLE_USER)
                .build();
    }
}
