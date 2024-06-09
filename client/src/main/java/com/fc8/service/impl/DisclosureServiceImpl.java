package com.fc8.service.impl;

import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.disclosure.DisclosureEmoji;
import com.fc8.platform.domain.entity.disclosure.DisclosureFile;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.repository.*;
import com.fc8.service.DisclosureService;
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
public class DisclosureServiceImpl implements DisclosureService {

    private final MemberRepository memberRepository;
    private final DisclosureRepository disclosureRepository;
    private final DisclosureEmojiRepository disclosureEmojiRepository;
    private final DisclosureFileRepository disclosureFileRepository;
    private final DisclosureCommentRepository disclosureCommentRepository;

    @Override
    @Transactional(readOnly = true)
    public DisclosureDetailInfo loadDisclosureDetail(Long memberId, Long disclosureId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var disclosure = disclosureRepository.getDisclosureWithCategoryByIdAndApartCode(disclosureId, apartCode);

        final EmojiCountInfo emojiCount = disclosureEmojiRepository.getEmojiCountInfoByDisclosureAndMember(disclosure);
        final EmojiReactionInfo emojiReaction = disclosureEmojiRepository.getEmojiReactionInfoByDisclosureAndMember(disclosure, member);

        return DisclosureDetailInfo.fromEntity(disclosure, disclosure.getAdmin(), disclosure.getCategory(), emojiCount, emojiReaction);
    }

    @Override
    public List<DisclosureFileInfo> loadDisclosureFileList(Long disclosureId, String apartCode) {
        // 게시글 조회
        var disclosure = disclosureRepository.getDisclosureWithCategoryByIdAndApartCode(disclosureId, apartCode);

        // 파일 조회
        final List<DisclosureFile> disclosureFileList = disclosureFileRepository.getDisclosureFileListByDisclosure(disclosure);

        return disclosureFileList.stream().map(DisclosureFileInfo::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisclosureInfo> loadDisclosureList(Long memberId, String apartCode, SearchPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 게시글 조회 (아파트 코드, 차단 사용자)
        var disclosureList = disclosureRepository.getDisclosureListByApartCode(memberId, apartCode, pageable, command.search(), command.type(), command.categoryCode());
        final List<DisclosureInfo> disclosureInfoList = disclosureList.stream()
            .map(disclosure -> DisclosureInfo.fromEntity(disclosure, disclosure.getAdmin(), disclosure.getCategory()))
            .toList();

        return new PageImpl<>(disclosureInfoList, pageable, disclosureList.getTotalElements());
    }

    @Override
    public Page<DisclosureCommentInfo> loadCommentList(Long memberId, String apartCode, Long disclosureId, CustomPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 댓글 조회
        return disclosureCommentRepository.getCommentListByDisclosure(disclosureId, pageable);
    }

    @Override
    @Transactional
    public EmojiInfo registerEmoji(Long memberId, Long disclosureId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var disclosure = disclosureRepository.getDisclosureWithCategoryByIdAndApartCode(disclosureId, apartCode);

        // 3. 레코드 검사 (이미 등록된 경우 삭제 요청이 필요하다.)
        boolean affected = disclosureEmojiRepository.existsByDisclosureAndMemberAndEmoji(disclosure, member, emoji);
        if (affected) {
            throw new BaseException(ErrorCode.ALREADY_REGISTER_EMOJI);
        }

        var disclosureEmoji = DisclosureEmoji.create(disclosure, member, emoji);
        var newDisclosureEmoji = disclosureEmojiRepository.store(disclosureEmoji);

        return EmojiInfo.fromDisclosureEmojiEntity(newDisclosureEmoji);
    }

    @Override
    @Transactional
    public void deleteEmoji(Long memberId, Long disclosureId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var disclosure = disclosureRepository.getDisclosureWithCategoryByIdAndApartCode(disclosureId, apartCode);

        // 3. 레코드 검사 (등록된 감정 표현이 없을 경우 등록이 필요하다.)
        DisclosureEmoji disclosureEmoji = disclosureEmojiRepository.getByDisclosureAndMemberAndEmoji(disclosure, member, emoji);

        // 4. 삭제
        disclosureEmojiRepository.delete(disclosureEmoji);
    }

    @Override
    public List<SearchDisclosureInfo> searchDisclosureList(String apartCode, String keyword, int pinnedDisclosureCount) {
        if (pinnedDisclosureCount >= 5) return null;

        List<Disclosure> disclosureList = disclosureRepository.getDisclosureListByKeyword(apartCode, keyword, pinnedDisclosureCount);

        return disclosureList.stream()
            .map(disclosure -> SearchDisclosureInfo.fromDisclosure(disclosure, disclosure.getAdmin(), disclosure.getCategory()))
            .toList();
    }
}

