package com.fc8.service.impl;

import com.fc8.platform.common.properties.NotificationProperties;
import com.fc8.platform.domain.enums.ProcessingStatus;
import com.fc8.platform.dto.command.WriteQnaAnswerCommand;
import com.fc8.platform.dto.notification.QnaNotification;
import com.fc8.platform.dto.notification.web.QnaAnswerWebPushInfo;
import com.fc8.platform.repository.AdminRepository;
import com.fc8.platform.repository.NotificationRepository;
import com.fc8.platform.repository.QnaAnswerRepository;
import com.fc8.platform.repository.QnaRepository;
import com.fc8.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {

    private final AdminRepository adminRepository;
    private final QnaRepository qnaRepository;
    private final QnaAnswerRepository qnaAnswerRepository;
    private final NotificationRepository notificationRepository;

    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void changeStatus(Long adminId, Long qnaId, String apartCode, ProcessingStatus processingStatus) {
        // 1. 관리자 조회
        var admin = adminRepository.getById(adminId);

        // 2. 민원 조회
        var qna = qnaRepository.getById(qnaId);

        // 3. 처리 상태 변경
        qna.changeStatus(processingStatus);
    }

    @Override
    @Transactional
    public Long writeAnswer(Long adminId, Long qnaId, String apartCode, WriteQnaAnswerCommand command) {
        // 1. 관리자 조회
        var admin = adminRepository.getById(adminId);

        // 2. 민원 조회
        var qna = qnaRepository.getById(qnaId);

        // 3. 답변 저장
        var qnaAnswer = command.toEntity(qna, admin);

        // 4. 알림 등록
        QnaNotification qnaNotification = QnaNotification.onlyWebPush(QnaAnswerWebPushInfo.fromQnaEntity(
                NotificationProperties.QNA_ANSWER_TITLE,
                NotificationProperties.getQnaAnswerContent(qna.getId()),
                qna));

        publisher.publishEvent(qnaNotification);

        return qnaAnswerRepository.store(qnaAnswer).getId();
    }

}
