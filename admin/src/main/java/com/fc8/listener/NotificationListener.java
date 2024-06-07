package com.fc8.listener;

import com.fc8.platform.dto.notification.QnaNotification;
import com.fc8.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @TransactionalEventListener
    public void sendQnaAnswer(QnaNotification notification) {
        if (notification == null) {
            return;
        }

        notificationService.sendQnaAnswerWebPush(notification.qnaAnswerWebPushInfo());
    }
}
