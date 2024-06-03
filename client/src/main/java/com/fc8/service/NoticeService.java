package com.fc8.service;

import com.fc8.platform.dto.record.NoticeDetailInfo;

public interface NoticeService {
    NoticeDetailInfo loadNoticeDetail(Long memberId, Long noticeId, String apartCode);
}
