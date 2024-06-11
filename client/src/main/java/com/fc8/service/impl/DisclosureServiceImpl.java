package com.fc8.service.impl;

import com.fc8.external.service.S3UploadService;
import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.disclosure.DisclosureCommentImage;
import com.fc8.platform.domain.entity.disclosure.DisclosureEmoji;
import com.fc8.platform.domain.entity.disclosure.DisclosureFile;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteDisclosureCommentCommand;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisclosureServiceImpl implements DisclosureService {

    private final MemberRepository memberRepository;
    private final DisclosureRepository disclosureRepository;
    private final DisclosureEmojiRepository disclosureEmojiRepository;
    private final DisclosureFileRepository disclosureFileRepository;
    private final DisclosureCommentRepository disclosureCommentRepository;
    private final DisclosureCommentImageRepository disclosureCommentImageRepository;

    private final S3UploadService s3UploadService;

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
        return disclosureRepository.getDisclosureInfoList(memberId, apartCode, pageable, command.search(), command.type(), command.categoryCode());
    }

    @Override
    public Page<CommentInfo> loadCommentList(Long memberId, String apartCode, Long disclosureId, CustomPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 댓글 조회
        var disclosureCommentList = disclosureCommentRepository.getAllByDisclosureIdAndMemberId(disclosureId, memberId, pageable);
        final List<CommentInfo> commentInfoList = disclosureCommentList.stream()
            .map(comment -> CommentInfo.fromEntity(comment, comment.getDisclosureCommentImages(), comment.getAdmin(), comment.getMember()))
            .toList();

        return new PageImpl<>(commentInfoList, pageable, disclosureCommentList.getTotalElements());
    }

    @Override
    @Transactional
    public EmojiInfo registerEmoji(Long memberId, Long disclosureId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var disclosure = disclosureRepository.getDisclosureWithCategoryByIdAndApartCode(disclosureId, apartCode);

        // 3. 레코드 검사 (이미 등록된 경우 삭제 요청이 필요하다.)
        boolean affected = disclosureEmojiRepository.existsByDisclosureAndMemberAndEmoji(disclosure, member);
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
    @Transactional(readOnly = true)
    public List<SearchDisclosureInfo> searchDisclosureList(String apartCode, String keyword, int pinnedDisclosureCount) {
        if (pinnedDisclosureCount >= 5) return null;

        List<Disclosure> disclosureList = disclosureRepository.getDisclosureListByKeyword(apartCode, keyword, pinnedDisclosureCount);

        return disclosureList.stream()
            .map(disclosure -> SearchDisclosureInfo.fromDisclosure(disclosure, disclosure.getAdmin(), disclosure.getCategory()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getDisclosureCount(String apartCode, String keyword) {
        return disclosureRepository.getDisclosureCountByKeyword(apartCode, keyword);
    }

    @Override
    @Transactional
    public Long writeReply(Long memberId, Long disclosureId, String apartCode, WriteDisclosureCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 공개글 및 답글 조회
        Long commentId = Optional.ofNullable(command.getParentId())
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));

        var disclosure = disclosureRepository.getByIdAndApartCode(disclosureId, apartCode);
        var disclosureComment = disclosureCommentRepository.getByIdAndDisclosure(commentId, disclosure);

        // 3. 답글 저장
        var disclosureReply = command.toEntity(disclosure, disclosureComment, member);
        var newDisclosureReply = disclosureCommentRepository.store(disclosureReply);

        // 4. 이미지 저장
        Optional.ofNullable(image)
            .filter(img -> !img.isEmpty())
            .ifPresent(img -> {
                UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                var disclosureCommentImage = DisclosureCommentImage.create(disclosureComment, uploadImageInfo.originalImageUrl());
                disclosureCommentImageRepository.store(disclosureCommentImage);
            });

        return newDisclosureReply.getId();
    }

    @Override
    public Long writeComment(Long memberId, Long disclosureId, String apartCode, WriteDisclosureCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 공개자료 조회
        var disclosure = disclosureRepository.getByIdAndApartCode(disclosureId, apartCode);

        // 3. 댓글 저장
        var disclosureComment = command.toEntity(disclosure, member);
        var newDisclosureComment = disclosureCommentRepository.store(disclosureComment);

        // 4. 댓글 이미지 저장
        Optional.ofNullable(image)
            .filter(img -> !img.isEmpty())
            .ifPresent(img -> {
                UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                var disclosureCommentImage = DisclosureCommentImage.create(disclosureComment, uploadImageInfo.originalImageUrl());
                disclosureCommentImageRepository.store(disclosureCommentImage);
            });

        return newDisclosureComment.getId();
    }

    @Override
    @Transactional
    public Long modifyComment(Long memberId, Long disclosureId, Long commentId, String apartCode, WriteDisclosureCommentCommand command, MultipartFile image) {
        // 1. 댓글 조회
        var disclosureComment = disclosureCommentRepository.getByIdAndDisclosureIdAndMemberId(commentId, disclosureId, memberId);

        // 2. 댓글 이미지 조회
        var disclosureCommentImage = disclosureCommentImageRepository.getImageByDisclosureCommentId(commentId);

        // 3. 댓글 수정
        disclosureComment.modify(command.getContent());

        // 4. 기존 이미지가 있는 경우 기존 이미지 삭제 및 변경
        if (disclosureCommentImage != null) {
            disclosureCommentImage.delete();
        }

        // 5. 새로운 이미지가 있는 경우 이미지 저장
        Optional.ofNullable(image)
            .filter(img -> !img.isEmpty())
            .ifPresent(img -> {
                UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                var newDisclosureCommentImage = DisclosureCommentImage.create(disclosureComment, uploadImageInfo.originalImageUrl());
                disclosureCommentImageRepository.store(newDisclosureCommentImage);
            });

        return disclosureComment.getId();
    }

    @Override
    @Transactional
    public Long deleteComment(Long memberId, Long disclosureId, Long commentId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 공개자료 조회
        var disclosure = disclosureRepository.getByIdAndApartCode(disclosureId, apartCode);

        // 3. 댓글 조회
        var comment = disclosureCommentRepository.getByIdAndDisclosure(commentId, disclosure);

        // 4. 본인 작성 댓글 여부 확인
        boolean affected = disclosureCommentRepository.isWriter(comment, member);
        if (!affected) {
            throw new InvalidParamException(ErrorCode.NOT_POST_COMMENT_WRITER);
        }

        // 5. 댓글 삭제
        comment.delete();

        return commentId;
    }
}

