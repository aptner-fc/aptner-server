package com.fc8.platform.dto.command;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaComment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriteQnaCommentCommand {

    private final Long parentId;

    private final String content;

    public QnaComment toEntity(Qna qna, Member member) {
        return QnaComment.createComment(qna, member, content);
    }

    public QnaComment toEntity(Qna qna, QnaComment parent, Member member) {
        return QnaComment.createReply(qna, parent, member, content);
    }

}
