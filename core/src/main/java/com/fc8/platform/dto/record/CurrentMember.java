package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.member.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record CurrentMember(Long id,
                            String email,
                            String phone,
                            String nickname,
                            ApartInfo mainApartInfo,
                            List<ApartInfo> apartInfoList) {
    public static CurrentMember fromEntityWithApartInfo(Member member, ApartInfo apartInfo, List<ApartInfo> apartInfoList) {
        return CurrentMember.builder()
                .id(member.getId())
                .email(member.getEmail())
                .phone(member.getPhone())
                .nickname(member.getNickname())
                .mainApartInfo(apartInfo)
                .apartInfoList(apartInfoList)
                .build();
    }

}
