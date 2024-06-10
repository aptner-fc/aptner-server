package com.fc8.platform.dto.command;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.notice.NoticeComment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriteNoticeCommentCommand {

    private final Long parentId;
    private final String content;

    public NoticeComment toEntity(Notice notice, Member member) {
        return NoticeComment.createComment(notice, member, content);
    }

    public NoticeComment toEntity(Notice notice, NoticeComment parent, Member member) {
        return NoticeComment.createReply(notice, parent, member, content);
    }

}
