package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.terms.Terms;
import com.fc8.platform.domain.enums.TermsType;
import lombok.Builder;

import java.util.List;

@Builder
public record UsedTermsInfo(Long id,
                            String title,
                            String content,
                            TermsType type,
                            boolean isRequired) {

    public static UsedTermsInfo fromEntity(Terms terms) {
        return UsedTermsInfo.builder()
                .id(terms.getId())
                .title(terms.getTitle())
                .content(terms.getContent())
                .type(terms.getType())
                .isRequired(terms.isRequired())
                .build();
    }

    public static List<UsedTermsInfo> fromEntityList(List<Terms> termsList) {
        return termsList.stream()
                .map(UsedTermsInfo::fromEntity)
                .toList();
    }

}
