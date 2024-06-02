package com.fc8.platform.dto.command;

import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriteQnaCommand {

    private final String categoryCode;

    private final String title;

    private final String content;

    private final boolean isPrivate;

    public Qna toEntity(Category category, Member member, Apart apart) {
        return Qna.create(category, member, apart, title, content, isPrivate);
    }

}
