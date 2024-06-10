package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.notice.NoticeComment;
import com.fc8.platform.dto.record.NoticeCommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeCommentRepository {
    Page<NoticeCommentInfo> getCommentListByQna(Long noticeId, Pageable pageable);

    NoticeComment getByIdAndNotice(Long commentId, Notice notice);

    NoticeComment store(NoticeComment noticeComment);

    NoticeComment getByIdAndNoticeIdAndMemberId(Long commentId, Long noticeId, Long memberId);
}
