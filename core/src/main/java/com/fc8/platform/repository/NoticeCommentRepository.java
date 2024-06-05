package com.fc8.platform.repository;

import com.fc8.platform.dto.record.NoticeCommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeCommentRepository {
    Page<NoticeCommentInfo> getCommentListByQna(Long noticeId, Pageable pageable);
}
