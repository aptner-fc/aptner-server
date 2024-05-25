package com.fc8.service;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WritePostCommand;
import com.fc8.platform.dto.command.WritePostCommentCommand;
import com.fc8.platform.dto.record.EmojiInfo;
import com.fc8.platform.dto.record.PostDetailInfo;
import com.fc8.platform.dto.record.PostInfo;
import com.fc8.platform.dto.record.SearchPageCommand;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    Long write(Long memberId, String apartCode, WritePostCommand command, MultipartFile image);

    Page<PostInfo> loadPostList(Long memberId, String apartCode, SearchPageCommand command);

    Long writeComment(Long memberId, Long postId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    Long writeReply(Long memberId, Long postId, String apartCode, WritePostCommentCommand command, MultipartFile image);

    PostDetailInfo loadPostDetail(Long memberId, Long postId, String apartCode);

    EmojiInfo registerEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji);

    void deleteEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji);
}