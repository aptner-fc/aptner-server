package com.fc8.service;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteQnaCommand;
import com.fc8.platform.dto.command.WriteQnaCommentCommand;
import com.fc8.platform.dto.record.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface QnaService {

    Long create(Long memberId, String apartCode, WriteQnaCommand command, MultipartFile image);

    Page<QnaInfo> loadQnaList(Long memberId, String apartCode, SearchPageCommand command);

    Long deleteQna(Long memberId, Long qnaId, String apartCode);

    QnaDetailInfo loadQnaDetail(Long memberId, Long qnaId, String apartCode);

    EmojiInfo registerEmoji(Long memberId, Long qnaId, String apartCode, EmojiType emoji);

    void deleteEmoji(Long memberId, Long qnaId, String apartCode, EmojiType emoji);

    Long writeReply(Long memberId, Long qnaId, String apartCode, WriteQnaCommentCommand command, MultipartFile image);

    Long writeComment(Long memberId, Long qnaId, String apartCode, WriteQnaCommentCommand command, MultipartFile image);

    Long deleteComment(Long memberId, Long qnaId, Long qnaCommentId, String apartCode);

    Page<QnaCommentInfo> loadCommentList(Long memberId, String apartCode, Long qnaId, SearchPageCommand command);
}
