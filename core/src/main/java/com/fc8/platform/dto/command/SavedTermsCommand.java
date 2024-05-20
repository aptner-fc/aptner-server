package com.fc8.platform.dto.command;


import com.fc8.platform.domain.entity.terms.Terms;
import com.fc8.platform.domain.enums.TermsType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SavedTermsCommand {

    private final String title;

    private final String content;

    private final TermsType type;

    private final boolean isUsed;

    private final boolean isRequired;

    public Terms toEntity() {
        return Terms.create(title, content, type, isUsed, isRequired);
    }

}
