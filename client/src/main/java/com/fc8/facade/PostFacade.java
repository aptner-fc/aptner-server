package com.fc8.facade;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WritePostCommand;
import com.fc8.platform.dto.command.WritePostCommentCommand;
import com.fc8.platform.dto.record.PostInfo;
import com.fc8.platform.dto.record.SearchPageCommand;
import com.fc8.platform.dto.response.*;
import com.fc8.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
//    private final PinnedPostService pinnedPostService;

    public WritePostResponse writePost(Long memberId, String apartCode, WritePostCommand command, MultipartFile image) {
        return new WritePostResponse(postService.writePost(memberId, apartCode, command, image));
    }

    public WritePostResponse modifyPost(Long memberId, Long postId, String apartCode, WritePostCommand command, MultipartFile image) {
        return new WritePostResponse(postService.modifyPost(memberId, postId, apartCode, command, image));
    }

    @Transactional(readOnly = true)
    public PageResponse<LoadPostListResponse> loadPostList(Long memberId, String apartCode, SearchPageCommand command) {
        // 1. 소통 게시판 게시물 조회
        final Page<PostInfo> posts = postService.loadPostList(memberId, apartCode, command);

        // 2. 상단 고정 게시물 조회

        return new PageResponse<>(posts, new LoadPostListResponse(posts.getContent(), null));
    }

    public WritePostCommentResponse writeComment(Long memberId, Long postId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
        return new WritePostCommentResponse(
                Optional.ofNullable(command.getParentId())
                        .map(parentId -> postService.writeReply(memberId, postId, apartCode, command, image))
                        .orElseGet(() -> postService.writeComment(memberId, postId, apartCode, command, image))
        );
    }

    public WritePostCommentResponse modifyComment(Long memberId, Long postId, Long commentId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
        return new WritePostCommentResponse(postService.modifyComment(memberId, postId, commentId, apartCode, command, image));
    }

    public LoadPostDetailResponse loadPostDetail(Long memberId, Long postId, String apartCode) {
        return new LoadPostDetailResponse(postService.loadPostDetail(memberId, postId, apartCode));
    }

    public RegisterEmojiResponse registerEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji) {
        return new RegisterEmojiResponse(postService.registerEmoji(memberId, postId, apartCode, emoji));
    }

    public DeletePostResponse deletePost(Long memberId, Long postId, String apartCode) {
        return new DeletePostResponse(postService.deletePost(memberId, postId, apartCode));
    }

    public void deleteEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji) {
        postService.deleteEmoji(memberId, postId, apartCode, emoji);
    }


}
