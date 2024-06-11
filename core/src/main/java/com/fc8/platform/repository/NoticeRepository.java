package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.enums.SearchType;
import com.fc8.platform.dto.record.NoticeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeRepository {
    Notice getNoticeWithCategoryByIdAndApartCode(Long noticeId, String apartCode);

    List<Notice> getNoticeListByKeyword(String apartCode, String keyword, int pinnedNoticeCount);

    Long getNoticeCountByKeyword(String apartCode, String keyword);

    Notice getByIdAndApartCode(Long noticeId, String apartCode);

    Page<NoticeInfo> getNoticeInfoList(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode);

    void updateViewCount(Long noticeId, Long viewCount);
}
