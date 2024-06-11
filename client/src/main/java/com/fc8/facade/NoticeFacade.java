package com.fc8.facade;

import com.fc8.platform.common.properties.AptnerProperties;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteNoticeCommentCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.dto.response.*;
import com.fc8.service.NoticeService;
import com.fc8.service.PinnedPostService;
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
    private final PinnedPostService pinnedPostService;

    @Transactional(readOnly = true)
    public LoadNoticeDetailResponse loadNoticeDetail(Long memberId, Long noticeId, String apartCode) {
        final NoticeDetailInfo noticeDetailInfo = noticeService.loadNoticeDetail(memberId, noticeId, apartCode);
        final List<NoticeFileInfo> noticeFileList = noticeService.loadNoticeFileList(noticeId, apartCode);
        return new LoadNoticeDetailResponse(noticeDetailInfo, noticeFileList);
    }

    @Transactional(readOnly = true)
    public PageResponse<LoadNoticeListResponse> loadNoticeList(Long memberId, String apartCode, SearchPageCommand command) {
        // 일반 게시물 조회
        final Page<NoticeInfo> noticeList = noticeService.loadNoticeList(memberId, apartCode, command);

        // 상단 게시물(중요글) 조회
        List<PinnedPostSummary> pinnedNoticeList = pinnedPostService.loadPinnedPostList(apartCode, AptnerProperties.CATEGORY_CODE_NOTICE);

        return new PageResponse<>(noticeList, new LoadNoticeListResponse(noticeList.getContent(), pinnedNoticeList));
    }

    public PageResponse<LoadNoticeCommentListResponse> loadCommentList(Long memberId, String apartCode, Long noticeId, CustomPageCommand command) {
        final Page<CommentInfo> commentList = noticeService.loadCommentList(memberId, apartCode, noticeId, command);
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

    public WriteNoticeCommentResponse modifyComment(Long memberId, Long noticeId, Long commentId, String apartCode, WriteNoticeCommentCommand command, MultipartFile image) {
        return new WriteNoticeCommentResponse(noticeService.modifyComment(memberId, noticeId, commentId, apartCode, command, image));
    }

    public DeleteNoticeCommentResponse deleteComment(Long memberId, Long noticeId, Long commentId, String apartCode) {
        return new DeleteNoticeCommentResponse(noticeService.deleteComment(memberId, noticeId, commentId, apartCode));
    }
}
