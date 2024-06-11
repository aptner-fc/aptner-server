package com.fc8.listener;

import com.fc8.platform.dto.notification.PostNotification;
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
    public void sendQnaComment(QnaNotification notification) {
        if (notification == null) {
            return;
        }

        notificationService.sendQnaCommentWebPush(notification.qnaAnswerWebPushInfo());
    }

    @TransactionalEventListener
    public void sendPostComment(PostNotification notification) {
        if (notification == null) {
            return;
        }

        notificationService.sendPostCommentWebPush(notification.postCommentWebPushInfo());
    }

}
