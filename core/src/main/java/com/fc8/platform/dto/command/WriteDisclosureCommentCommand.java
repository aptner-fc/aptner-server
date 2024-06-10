package com.fc8.platform.dto.command;

import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.disclosure.DisclosureComment;
import com.fc8.platform.domain.entity.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriteDisclosureCommentCommand {

    private final Long parentId;
    private final String content;

    public DisclosureComment toEntity(Disclosure disclosure, Member member) {
        return DisclosureComment.createComment(disclosure, member, content);
    }

    public DisclosureComment toEntity(Disclosure disclosure, DisclosureComment parent, Member member) {
        return DisclosureComment.createReply(disclosure, parent, member, content);
    }

}
