package com.fc8.platform.dto.notification;

import com.fc8.platform.dto.notification.web.PostCommentWebPushInfo;
import lombok.Builder;

@Builder
public record PostNotification(
    PostCommentWebPushInfo postCommentWebPushInfo
) {

    public static PostNotification onlyWebPush(PostCommentWebPushInfo postCommentWebPushInfo) {
        return PostNotification.builder()
            .postCommentWebPushInfo(postCommentWebPushInfo)
            .build();
    }

}
