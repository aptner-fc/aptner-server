package com.fc8.facade;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteNoticeCommentCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.dto.response.*;
import com.fc8.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeFacade {

    private final NoticeService noticeService;

    @Transactional(readOnly = true)
    public LoadNoticeDetailResponse loadNoticeDetail(Long memberId, Long noticeId, String apartCode) {
        final NoticeDetailInfo noticeDetailInfo = noticeService.loadNoticeDetail(memberId, noticeId, apartCode);
        final List<NoticeFileInfo> noticeFileList = noticeService.loadNoticeFileList(noticeId, apartCode);
        return new LoadNoticeDetailResponse(noticeDetailInfo, noticeFileList);
    }

    @Transactional(readOnly = true)
    public PageResponse<LoadNoticeListResponse> loadNoticeList(Long memberId, String apartCode, SearchPageCommand command) {
        final Page<NoticeInfo> noticeList = noticeService.loadNoticeList(memberId, apartCode, command);
        return new PageResponse<>(noticeList, new LoadNoticeListResponse(noticeList.getContent()));
    }

    public PageResponse<LoadNoticeCommentListResponse> loadCommentList(Long memberId, String apartCode, Long noticeId, CustomPageCommand command) {
        final Page<NoticeCommentInfo> commentList = noticeService.loadCommentList(memberId, apartCode, noticeId, command);
        return new PageResponse<>(commentList, new LoadNoticeCommentListResponse(commentList.getContent()));
    }

    public RegisterEmojiResponse registerEmoji(Long memberId, Long noticeId, String apartCode, EmojiType emoji) {
        return new RegisterEmojiResponse(noticeService.registerEmoji(memberId, noticeId, apartCode, emoji));
    }

    public void deleteEmoji(Long memberId, Long noticeId, String apartCode, EmojiType emoji) {
        noticeService.deleteEmoji(memberId, noticeId, apartCode, emoji);
    }

    public WriteNoticeCommentResponse writeComment(Long memberId, Long noticeId, String apartCode, WriteNoticeCommentCommand command, MultipartFile image) {
        return new WriteNoticeCommentResponse(
            Optional.ofNullable(command.getParentId())
                .map(parentId -> noticeService.writeReply(memberId, noticeId, apartCode, command, image))
                .orElseGet(() -> noticeService.writeComment(memberId, noticeId, apartCode, command, image))
        );
    }
}
