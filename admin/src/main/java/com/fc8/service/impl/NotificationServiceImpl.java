package com.fc8.service.impl;

import com.fc8.platform.domain.entity.mapping.NotificationMemberMapping;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.notification.Notification;
import com.fc8.platform.domain.entity.notification.NotificationHistory;
import com.fc8.platform.dto.notification.web.QnaAnswerWebPushInfo;
import com.fc8.platform.repository.MemberRepository;
import com.fc8.platform.repository.NotificationHistoryRepository;
import com.fc8.platform.repository.NotificationMemberMappingRepository;
import com.fc8.platform.repository.NotificationRepository;
import com.fc8.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationMemberMappingRepository notificationMemberMappingRepository;


    @Override
    @Transactional
    public void sendQnaAnswerWebPush(QnaAnswerWebPushInfo webPushInfo) {
        if (webPushInfo == null) {
            return;
        }

        Member member = memberRepository.getActiveMemberById(webPushInfo.memberId());
        var notification = Notification.createBySystem(
                webPushInfo.title(),
                webPushInfo.content(),
                webPushInfo.notificationType(),
                webPushInfo.messageType());

        var notificationHistory = NotificationHistory.createWebPushHistory(notification);
        var notificationMemberMapping = NotificationMemberMapping.create(notification, member);

        notificationRepository.store(notification);
        notificationHistoryRepository.store(notificationHistory);
        notificationMemberMappingRepository.store(notificationMemberMapping);
    }
}