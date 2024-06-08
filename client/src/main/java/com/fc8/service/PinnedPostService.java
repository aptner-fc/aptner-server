package com.fc8.service;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WritePostCommentCommand;
import com.fc8.platform.dto.record.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PinnedPostService {

    PinnedPostDetailInfo loadPinnedPostDetail(Long memberId, String apartCode, String categoryCode, Long pinnedPostId);

    List<PostFileInfo> loadPinnedPostFileList(Long pinnedPostId, String apartCode);

    Page<CommentInfo> loadPinnedPostCommentList(Long memberId, String apartCode, Long pinnedPostId, CustomPageCommand command);

    Long writeComment(Long memberId, Long pinnedPostId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    Long writeReply(Long memberId, Long pinnedPostId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    Long modifyComment(Long memberId, Long pinnedPostId, Long commentId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    EmojiInfo registerEmoji(Long memberId, Long pinnedPostId, String apartCode, EmojiType emoji);

    void deleteEmoji(Long memberId, Long pinnedPostId, String apartCode, EmojiType emoji);
}
