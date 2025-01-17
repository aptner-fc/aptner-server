package com.fc8.service;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WritePostCommand;
import com.fc8.platform.dto.command.WritePostCommentCommand;
import com.fc8.platform.dto.record.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    Long writePost(Long memberId, String apartCode, WritePostCommand command, MultipartFile image, List<MultipartFile> files);

    Long modifyPost(Long memberId, Long postId, String apartCode, WritePostCommand command, MultipartFile image);

    Page<PostSummary> loadPostList(Long memberId, String apartCode, Long apartAreaId, SearchPageCommand command);

    Long writeComment(Long memberId, Long postId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    Long writeReply(Long memberId, Long postId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    Long modifyComment(Long memberId, Long postId, Long commentId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    PostDetailInfo loadPostDetail(Long memberId, Long postId, String apartCode);

    EmojiInfo registerEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji);

    Long deletePost(Long memberId, Long postId, String apartCode);

    void deleteEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji);

    Long deleteComment(Long memberId, Long postId, Long commentId, String apartCode);

    Page<CommentInfo> loadCommentList(Long memberId, String apartCode, Long postId, CustomPageCommand command);

    List<PostFileInfo> loadPostFileList(Long postId, String apartCode);

    List<ApartAreaSummary> loadApartArea(String apartCode);

    List<SearchPostInfo> searchPostList(Long memberId, String apartCode, String keyword, int pinnedPostCount);

    Long getPostCount(Long memberId, String apartCode, String keyword);

    void updateViewCount(Long postId, Long viewCount);
}
