package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.notice.Notice;

public interface NoticeRepository {
    Notice getNoticeWithCategoryByIdAndApartCode(Long noticeId, String apartCode);
}
