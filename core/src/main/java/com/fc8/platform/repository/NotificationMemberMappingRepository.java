package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.mapping.NotificationMemberMapping;

public interface NotificationMemberMappingRepository {

    NotificationMemberMapping store(NotificationMemberMapping notificationMemberMapping);

    void delete(NotificationMemberMapping notificationMemberMapping);

}
