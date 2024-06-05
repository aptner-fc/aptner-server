package com.fc8.platform.dto.command;

import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaAnswer;
import com.fc8.platform.domain.enums.ProcessingStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriteQnaAnswerCommand {

    private String content;
    private ProcessingStatus processingStatus;

    public QnaAnswer toEntity(Qna qna, Admin admin) {
        return QnaAnswer.createAnswer(qna, admin, content);
    }
}
