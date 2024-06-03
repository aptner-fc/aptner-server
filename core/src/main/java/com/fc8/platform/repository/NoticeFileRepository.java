package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.notice.NoticeFile;

import java.util.List;

public interface NoticeFileRepository {

    List<NoticeFile> getNoticeFileListByNotice(Notice notice);

}
