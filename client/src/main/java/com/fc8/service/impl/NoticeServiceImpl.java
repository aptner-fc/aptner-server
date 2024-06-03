package com.fc8.service.impl;

import com.fc8.platform.dto.record.NoticeDetailInfo;
import com.fc8.platform.repository.MemberRepository;
import com.fc8.platform.repository.NoticeEmojiRepository;
import com.fc8.platform.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
//    private final NoticeEmojiRepository noticeEmojiRepository;

//    @Override
//    @Transactional(readOnly = true)
//    public NoticeDetailInfo loadNoticeDetail(Long memberId, Long noticeId, String apartCode) {
//        // 1. 회원 조회
//        var member = memberRepository.getActiveMemberById(memberId);
//
//        // 2. 게시글 조회
//        var Notice = noticeRepository.getNoticeWithCategoryByIdAndApartCode(noticeId, apartCode);
//
//        return null;
//    }
}
