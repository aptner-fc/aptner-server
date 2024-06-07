package com.fc8.service;

import com.fc8.platform.domain.enums.ProcessingStatus;
import com.fc8.platform.dto.command.WriteQnaAnswerCommand;

public interface QnaService {

    void changeStatus(Long adminId, Long qnaId, String apartCode, ProcessingStatus processingStatus);

    Long writeAnswer(Long adminId, Long qnaId, String apartCode, WriteQnaAnswerCommand command);
}
