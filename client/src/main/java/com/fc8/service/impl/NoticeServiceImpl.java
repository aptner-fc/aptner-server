package com.fc8.service.impl;

import com.fc8.external.service.S3UploadService;
import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.notice.NoticeCommentImage;
import com.fc8.platform.domain.entity.notice.NoticeEmoji;
import com.fc8.platform.domain.entity.notice.NoticeFile;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteNoticeCommentCommand;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeEmojiRepository noticeEmojiRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final NoticeCommentRepository noticeCommentRepository;
    private final NoticeCommentImageRepository noticeCommentImageRepository;

    private final S3UploadService s3UploadService;

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

    @Override
    @Transactional
    public EmojiInfo registerEmoji(Long memberId, Long noticeId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var notice = noticeRepository.getNoticeWithCategoryByIdAndApartCode(noticeId, apartCode);

        // 3. 레코드 검사 (이미 등록된 경우 삭제 요청이 필요하다.)
        boolean affected = noticeEmojiRepository.existsByNoticeAndMemberAndEmoji(notice, member);
        if (affected) {
            throw new BaseException(ErrorCode.ALREADY_REGISTER_EMOJI);
        }

        var noticeEmoji = NoticeEmoji.create(notice, member, emoji);
        var newNoticeEmoji = noticeEmojiRepository.store(noticeEmoji);

        return EmojiInfo.fromNoticeEmojiEntity(newNoticeEmoji);
    }

    @Override
    @Transactional
    public void deleteEmoji(Long memberId, Long noticeId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var notice = noticeRepository.getNoticeWithCategoryByIdAndApartCode(noticeId, apartCode);

        // 3. 레코드 검사 (등록된 감정 표현이 없을 경우 등록이 필요하다.)
        NoticeEmoji noticeEmoji = noticeEmojiRepository.getByNoticeAndMemberAndEmoji(notice, member, emoji);

        // 4. 삭제
        noticeEmojiRepository.delete(noticeEmoji);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchNoticeInfo> searchNoticeList(String apartCode, String keyword, int pinnedNoticeCount) {
        if (pinnedNoticeCount >= 5) return null;

        List<Notice> noticeList = noticeRepository.getNoticeListByKeyword(apartCode, keyword, pinnedNoticeCount);

        return noticeList.stream()
            .map(notice -> SearchNoticeInfo.fromNotice(notice, notice.getAdmin(), notice.getCategory()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getNoticeCount(String apartCode, String keyword) {
        return noticeRepository.getNoticeCountByKeyword(apartCode, keyword);
    }

    @Override
    @Transactional
    public Long writeReply(Long memberId, Long noticeId, String apartCode, WriteNoticeCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 및 답글 조회
        Long commentId = Optional.ofNullable(command.getParentId())
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));

        var notice = noticeRepository.getByIdAndApartCode(noticeId, apartCode);
        var noticeComment = noticeCommentRepository.getByIdAndNotice(commentId, notice);

        // 3. 답글 저장
        var noticeReply = command.toEntity(notice, noticeComment, member);
        var newNoticeReply = noticeCommentRepository.store(noticeReply);

        // 4. 이미지 저장
        Optional.ofNullable(image)
            .filter(img -> !img.isEmpty())
            .ifPresent(img -> {
                UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                var noticeCommentImage = NoticeCommentImage.create(noticeComment, uploadImageInfo.originalImageUrl());
                noticeCommentImageRepository.store(noticeCommentImage);
            });

        return newNoticeReply.getId();
    }

    @Override
    @Transactional
    public Long writeComment(Long memberId, Long noticeId, String apartCode, WriteNoticeCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var notice = noticeRepository.getByIdAndApartCode(noticeId, apartCode);

        // 3. 댓글 저장
        var noticeComment = command.toEntity(notice, member);
        var newNoticeComment = noticeCommentRepository.store(noticeComment);

        // 4. 댓글 이미지 저장
        Optional.ofNullable(image)
            .filter(img -> !img.isEmpty())
            .ifPresent(img -> {
                UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                var noticeCommentImage = NoticeCommentImage.create(noticeComment, uploadImageInfo.originalImageUrl());
                noticeCommentImageRepository.store(noticeCommentImage);
            });

        return newNoticeComment.getId();

    }

    @Override
    @Transactional
    public Long modifyComment(Long memberId, Long noticeId, Long commentId, String apartCode, WriteNoticeCommentCommand command, MultipartFile image) {
        // 1. 댓글 조회
        var noticeComment = noticeCommentRepository.getByIdAndNoticeIdAndMemberId(commentId, noticeId, memberId);

        // 2. 댓글 이미지 조회
        var noticeCommentImage = noticeCommentImageRepository.getImageByNoticeCommentId(commentId);

        // 3. 댓글 수정
        noticeComment.modify(command.getContent());

        // 4. 기존 이미지가 있는 경우 기존 이미지 삭제 및 변경
        if (noticeCommentImage != null) {
            noticeCommentImage.delete();
        }

        // 5. 새로운 이미지가 있는 경우 이미지 저장
        Optional.ofNullable(image)
            .filter(img -> !img.isEmpty())
            .ifPresent(img -> {
                UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                var newNoticeCommentImage = NoticeCommentImage.create(noticeComment, uploadImageInfo.originalImageUrl());
                noticeCommentImageRepository.store(newNoticeCommentImage);
            });

        return noticeComment.getId();

    }

    @Override
    @Transactional
    public Long deleteComment(Long memberId, Long noticeId, Long commentId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var notice = noticeRepository.getByIdAndApartCode(noticeId, apartCode);

        // 3. 댓글 조회
        var comment = noticeCommentRepository.getByIdAndNotice(commentId, notice);

        // 4. 본인 작성 댓글 여부
        boolean affected = noticeCommentRepository.isWriter(comment, member);
        if (!affected) {
            throw new InvalidParamException(ErrorCode.NOT_POST_COMMENT_WRITER);
        }

        // 5. 삭제
        comment.delete();

        return commentId;
    }
}
