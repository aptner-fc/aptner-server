package com.fc8.service.impl;

import com.fc8.platform.domain.entity.notice.NoticeFile;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;
import com.fc8.platform.dto.record.NoticeDetailInfo;
import com.fc8.platform.dto.record.NoticeFileInfo;
import com.fc8.platform.repository.MemberRepository;
import com.fc8.platform.repository.NoticeEmojiRepository;
import com.fc8.platform.repository.NoticeFileRepository;
import com.fc8.platform.repository.NoticeRepository;
import com.fc8.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeEmojiRepository noticeEmojiRepository;
    private final NoticeFileRepository noticeFileRepository;

    @Override
    @Transactional(readOnly = true)
    public NoticeDetailInfo loadNoticeDetail(Long memberId, Long noticeId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var notice = noticeRepository.getNoticeWithCategoryByIdAndApartCode(noticeId, apartCode);

        final EmojiCountInfo emojiCount = noticeEmojiRepository.getEmojiCountInfoByNoticeAndMember(notice);
        final EmojiReactionInfo emojiReaction = noticeEmojiRepository.getEmojiReactionInfoByNoticeAndMember(notice, member);

        return NoticeDetailInfo.fromEntity(notice, notice.getAdmin(), notice.getCategory(), emojiCount, emojiReaction);
    }

    @Override
    public List<NoticeFileInfo> loadNoticeFileList(Long noticeId, String apartCode) {
        // 게시글 조회
        var notice = noticeRepository.getNoticeWithCategoryByIdAndApartCode(noticeId, apartCode);

        // 파일 조회
        final List<NoticeFile> noticeFileList = noticeFileRepository.getNoticeFileListByNotice(notice);

        return noticeFileList.stream().map(NoticeFileInfo::fromEntity).toList();
    }
}
