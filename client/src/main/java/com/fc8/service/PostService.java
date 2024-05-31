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

    Page<PostInfo> loadPostList(Long memberId, String apartCode, SearchPageCommand command);

    Long writeComment(Long memberId, Long postId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    Long writeReply(Long memberId, Long postId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    Long modifyComment(Long memberId, Long postId, Long commentId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    PostDetailInfo loadPostDetail(Long memberId, Long postId, String apartCode);

    EmojiInfo registerEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji);

    Long deletePost(Long memberId, Long postId, String apartCode);

    void deleteEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji);

    Long deleteComment(Long memberId, Long postId, Long commentId, String apartCode);

    Page<PostCommentInfo> loadCommentList(Long memberId, String apartCode, Long postId, SearchPageCommand command);
}
