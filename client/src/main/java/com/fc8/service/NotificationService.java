package com.fc8.service;

import com.fc8.platform.dto.notification.web.QnaAnswerWebPushInfo;

public interface NotificationService {

    void sendQnaAnswerWebPush(QnaAnswerWebPushInfo webPushInfo);
}
