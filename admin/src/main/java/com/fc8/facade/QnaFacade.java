package com.fc8.facade;

import com.fc8.platform.domain.enums.ProcessingStatus;
import com.fc8.platform.dto.command.WriteQnaAnswerCommand;
import com.fc8.platform.dto.response.WriteQnaAnswerResponse;
import com.fc8.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaFacade {

    private final QnaService qnaService;

    @Transactional
    public WriteQnaAnswerResponse writeAnswer(Long adminId, Long qnaId, String apartCode, WriteQnaAnswerCommand command) {
        qnaService.changeStatus(adminId, qnaId, apartCode, command.getProcessingStatus());
        return new WriteQnaAnswerResponse(qnaService.writeAnswer(adminId, qnaId, apartCode, command));
    }

    public void changeStatus(Long adminId, Long qnaId, String apartCode, ProcessingStatus status) {
        qnaService.changeStatus(adminId, qnaId, apartCode, status);
    }
}
