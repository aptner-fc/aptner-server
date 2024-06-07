package com.fc8.server.impl;

import com.fc8.platform.domain.entity.mapping.NotificationMemberMapping;
import com.fc8.platform.repository.NotificationMemberMappingRepository;
import com.fc8.server.NotificationMemberMappingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationMemberMappingRepositoryImpl implements NotificationMemberMappingRepository {

    private final NotificationMemberMappingJpaRepository notificationMemberMappingJpaRepository;

    @Override
    public NotificationMemberMapping store(NotificationMemberMapping notificationMemberMapping) {
        return notificationMemberMappingJpaRepository.save(notificationMemberMapping);
    }

    @Override
    public void delete(NotificationMemberMapping notificationMemberMapping) {
        notificationMemberMappingJpaRepository.delete(notificationMemberMapping);
    }
}
