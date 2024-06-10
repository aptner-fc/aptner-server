package com.fc8.service;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteDisclosureCommentCommand;
import com.fc8.platform.dto.record.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DisclosureService {
    DisclosureDetailInfo loadDisclosureDetail(Long memberId, Long disclosureId, String apartCode);

    List<DisclosureFileInfo> loadDisclosureFileList(Long disclosureId, String apartCode);

    Page<DisclosureInfo> loadDisclosureList(Long memberId, String apartCode, SearchPageCommand command);

    Page<CommentInfo> loadCommentList(Long memberId, String apartCode, Long disclosureId, CustomPageCommand command);

    EmojiInfo registerEmoji(Long memberId, Long disclosureId, String apartCode, EmojiType emoji);

    void deleteEmoji(Long memberId, Long disclosureId, String apartCode, EmojiType emoji);

    List<SearchDisclosureInfo> searchDisclosureList(String apartCode, String keyword, int pinnedDisclosureCount);

    Long getDisclosureCount(String apartCode, String keyword);

    Long writeReply(Long memberId, Long disclosureId, String apartCode, WriteDisclosureCommentCommand command, MultipartFile image);

    Long writeComment(Long memberId, Long disclosureId, String apartCode, WriteDisclosureCommentCommand command, MultipartFile image);

    Long modifyComment(Long memberId, Long disclosureId, Long commentId, String apartCode, WriteDisclosureCommentCommand command, MultipartFile image);

    Long deleteComment(Long memberId, Long disclosureId, Long commentId, String apartCode);
}

