package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.enums.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepository {
    Notice getNoticeWithCategoryByIdAndApartCode(Long noticeId, String apartCode);

    Page<Notice> getNoticeListByApartCode(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode);
}
