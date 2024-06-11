package com.fc8.service;

import com.fc8.platform.dto.notification.web.PostCommentWebPushInfo;
import com.fc8.platform.dto.notification.web.QnaAnswerWebPushInfo;

public interface NotificationService {
    void sendQnaCommentWebPush(QnaAnswerWebPushInfo qnaAnswerWebPushInfo);

    void sendPostCommentWebPush(PostCommentWebPushInfo postCommentWebPushInfo);
}
