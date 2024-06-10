package com.fc8.facade;

import com.fc8.platform.common.properties.AptnerProperties;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteDisclosureCommentCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.dto.response.*;
import com.fc8.service.DisclosureService;
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
public class DisclosureFacade {

    private final DisclosureService disclosureService;
    private final PinnedPostService pinnedPostService;

    @Transactional(readOnly = true)
    public LoadDisclosureDetailResponse loadDisclosureDetail(Long memberId, Long disclosureId, String apartCode) {
        final DisclosureDetailInfo disclosureDetailInfo = disclosureService.loadDisclosureDetail(memberId, disclosureId, apartCode);
        final List<DisclosureFileInfo> disclosureFileList = disclosureService.loadDisclosureFileList(disclosureId, apartCode);
        return new LoadDisclosureDetailResponse(disclosureDetailInfo, disclosureFileList);
    }

    @Transactional(readOnly = true)
    public PageResponse<LoadDisclosureListResponse> loadDisclosureList(Long memberId, String apartCode, SearchPageCommand command) {
        final Page<DisclosureInfo> disclosureList = disclosureService.loadDisclosureList(memberId, apartCode, command);
        // 2. 상단 고정 게시물(중요글) 조회
        List<PinnedPostSummary> pinnedPosts = pinnedPostService.loadPinnedPostList(apartCode, AptnerProperties.CATEGORY_CODE_DISCLOSURE);
        return new PageResponse<>(disclosureList, new LoadDisclosureListResponse(disclosureList.getContent(), pinnedPosts));
    }

    public PageResponse<LoadDisclosureCommentListResponse> loadCommentList(Long memberId, String apartCode, Long disclosureId, CustomPageCommand command) {
        final Page<CommentInfo> commentList = disclosureService.loadCommentList(memberId, apartCode, disclosureId, command);
        return new PageResponse<>(commentList, new LoadDisclosureCommentListResponse(commentList.getContent()));
    }

    public RegisterEmojiResponse registerEmoji(Long memberId, Long disclosureId, String apartCode, EmojiType emoji) {
        return new RegisterEmojiResponse(disclosureService.registerEmoji(memberId, disclosureId, apartCode, emoji));
    }

    public void deleteEmoji(Long memberId, Long disclosureId, String apartCode, EmojiType emoji) {
        disclosureService.deleteEmoji(memberId, disclosureId, apartCode, emoji);
    }

    public WriteDisclosureCommentResponse writeComment(Long memberId, Long disclosureId, String apartCode, WriteDisclosureCommentCommand command, MultipartFile image) {
        return new WriteDisclosureCommentResponse(
            Optional.ofNullable(command.getParentId())
                .map(parentId -> disclosureService.writeReply(memberId, disclosureId, apartCode, command, image))
                .orElseGet(() -> disclosureService.writeComment(memberId, disclosureId, apartCode, command, image))
        );

    }

    public WriteDisclosureCommentResponse modifyComment(Long memberId, Long disclosureId, Long commentId, String apartCode, WriteDisclosureCommentCommand command, MultipartFile image) {
        return new WriteDisclosureCommentResponse(disclosureService.modifyComment(memberId, disclosureId, commentId, apartCode, command, image));
    }

    public DeleteDisclosureCommentResponse deleteComment(Long memberId, Long disclosureId, Long commentId, String apartCode) {
        return new DeleteDisclosureCommentResponse(disclosureService.deleteComment(memberId, disclosureId, commentId, apartCode));
    }
}

