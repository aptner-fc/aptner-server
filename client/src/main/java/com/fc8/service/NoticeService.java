package com.fc8.service;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteNoticeCommentCommand;
import com.fc8.platform.dto.record.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    NoticeDetailInfo loadNoticeDetail(Long memberId, Long noticeId, String apartCode);

    List<NoticeFileInfo> loadNoticeFileList(Long noticeId, String apartCode);

    Page<NoticeInfo> loadNoticeList(Long memberId, String apartCode, SearchPageCommand command);

    Page<NoticeCommentInfo> loadCommentList(Long memberId, String apartCode, Long noticeId, CustomPageCommand command);

    EmojiInfo registerEmoji(Long memberId, Long noticeId, String apartCode, EmojiType emoji);

    void deleteEmoji(Long memberId, Long noticeId, String apartCode, EmojiType emoji);

    List<SearchNoticeInfo> searchNoticeList(String apartCode, String keyword, int pinnedNoticeCount);

    Long getNoticeCount(String apartCode, String keyword);

    Long writeReply(Long memberId, Long noticeId, String apartCode, WriteNoticeCommentCommand command, MultipartFile image);

    Long writeComment(Long memberId, Long noticeId, String apartCode, WriteNoticeCommentCommand command, MultipartFile image);

    Long modifyComment(Long memberId, Long noticeId, Long commentId, String apartCode, WriteNoticeCommentCommand command, MultipartFile image);
}
