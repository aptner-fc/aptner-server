package com.fc8.facade;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WritePostCommentCommand;
import com.fc8.platform.dto.record.CommentInfo;
import com.fc8.platform.dto.record.CustomPageCommand;
import com.fc8.platform.dto.record.PinnedPostDetailInfo;
import com.fc8.platform.dto.record.PostFileInfo;
import com.fc8.platform.dto.response.*;
import com.fc8.service.PinnedPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PinnedPostFacade {

    private final PinnedPostService pinnedPostService;

    @Transactional(readOnly = true)
    public LoadPinnedPostDetailResponse loadPinnedPostDetail(Long memberId, String apartCode, String categoryCode, Long pinnedPostId) {
        PinnedPostDetailInfo pinnedPostDetailInfo = pinnedPostService.loadPinnedPostDetail(memberId, apartCode, categoryCode, pinnedPostId);
        final List<PostFileInfo> pinnedPostFileInfoList = pinnedPostService.loadPinnedPostFileList(pinnedPostId, apartCode);
        return new LoadPinnedPostDetailResponse(pinnedPostDetailInfo, pinnedPostFileInfoList);
    }

    public PageResponse<LoadPinnedPostCommentListResponse> loadPinnedPostCommentList(Long memberId, String apartCode, Long postId, CustomPageCommand command) {
        final Page<CommentInfo> commentList = pinnedPostService.loadPinnedPostCommentList(memberId, apartCode, postId, command);
        return new PageResponse<>(commentList, new LoadPinnedPostCommentListResponse(commentList.getContent()));
    }

    public WritePostCommentResponse writeComment(Long memberId, String apartCode, Long pinnedPostId, WritePostCommentCommand command, MultipartFile image) {

        return new WritePostCommentResponse(
                Optional.ofNullable(command.getParentId())
                        .map(parentId -> pinnedPostService.writeReply(memberId, pinnedPostId, apartCode, command, image))
                        .orElseGet(() -> pinnedPostService.writeComment(memberId, pinnedPostId, apartCode, command, image))
        );
    }

    public WritePostCommentResponse modifyComment(Long memberId, String apartCode, Long pinnedPostId, Long commentId, WritePostCommentCommand command, MultipartFile image) {
        return new WritePostCommentResponse(pinnedPostService.modifyComment(memberId, pinnedPostId, commentId, apartCode, command, image));
    }

    public RegisterEmojiResponse registerEmoji(Long memberId, String apartCode, Long pinnedPostId, EmojiType emoji) {
        return new RegisterEmojiResponse(pinnedPostService.registerEmoji(memberId, pinnedPostId, apartCode, emoji));
    }

    public void deleteEmoji(Long memberId, String apartCode, Long pinnedPostId, EmojiType emoji) {
        pinnedPostService.deleteEmoji(memberId, pinnedPostId, apartCode, emoji);
    }

//
//    public WritePinnedPostCommentResponse modifyComment(Long memberId, Long postId, Long commentId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
//        return new WritePinnedPostCommentResponse(pinnedPostService.modifyComment(memberId, postId, commentId, apartCode, command, image));
//    }
//
//
//
//    public DeletePinnedPostResponse deletePost(Long memberId, Long postId, String apartCode) {
//        return new DeletePinnedPostResponse(pinnedPostService.deletePost(memberId, postId, apartCode));
//    }
//
//
//    public DeletePinnedPostCommentResponse deleteComment(Long memberId, Long postId, Long commentId, String apartCode) {
//        return new DeletePinnedPostCommentResponse(pinnedPostService.deleteComment(memberId, postId, commentId, apartCode));
//    }

}
