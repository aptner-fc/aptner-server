package com.fc8.service.impl;

import com.fc8.platform.domain.entity.notice.NoticeFile;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.repository.*;
import com.fc8.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final NoticeCommentRepository noticeCommentRepository;

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

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeInfo> loadNoticeList(Long memberId, String apartCode, SearchPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 게시글 조회 (아파트 코드, 차단 사용자)
        var noticeList = noticeRepository.getNoticeListByApartCode(memberId, apartCode, pageable, command.search(), command.type(), command.categoryCode());
        final List<NoticeInfo> noticeInfoList = noticeList.stream()
            .map(notice -> NoticeInfo.fromEntity(notice, notice.getAdmin(), notice.getCategory()))
            .toList();

        return new PageImpl<>(noticeInfoList, pageable, noticeList.getTotalElements());
    }

    @Override
    public Page<NoticeCommentInfo> loadCommentList(Long memberId, String apartCode, Long noticeId, CustomPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 댓글 조회
        return noticeCommentRepository.getCommentListByQna(noticeId, pageable);
    }
}
