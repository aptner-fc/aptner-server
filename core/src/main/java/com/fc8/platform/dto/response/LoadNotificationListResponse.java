package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.NotificationInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadNotificationListResponse {

    private final List<NotificationInfo> notificationList;

    public LoadNotificationListResponse(List<NotificationInfo> notificationList) {
        this.notificationList = notificationList;
    }
}
