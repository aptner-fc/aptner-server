package com.fc8.service.impl;

import com.fc8.external.service.S3UploadService;
import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.utils.ValidateUtils;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.pinned.PinnedPostCommentImage;
import com.fc8.platform.domain.entity.pinned.PinnedPostEmoji;
import com.fc8.platform.domain.entity.pinned.PinnedPostFile;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WritePostCommentCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.repository.*;
import com.fc8.service.PinnedPostService;
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
public class PinnedPostServiceImpl implements PinnedPostService {

    private final MemberRepository memberRepository;
    private final PinnedPostRepository pinnedPostRepository;
    private final PinnedPostEmojiRepository pinnedPostEmojiRepository;
    private final PinnedPostFileRepository pinnedPostFileRepository;
    private final PinnedPostCommentRepository pinnedPostCommentRepository;
    private final PinnedPostCommentImageRepository pinnedPostCommentImageRepository;

    private final S3UploadService s3UploadService;

    @Override
    @Transactional(readOnly = true)
    public PinnedPostDetailInfo loadPinnedPostDetail(Long memberId, String apartCode, String categoryCode, Long pinnedPostId) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var pinnedPost = pinnedPostRepository.getPinnedPostByIdAndApartCode(memberId, pinnedPostId, apartCode);
        ValidateUtils.validateParentCategoryCode(categoryCode, pinnedPost.getCategory());

        final EmojiCountInfo emojiCount = pinnedPostEmojiRepository.getEmojiCountInfoByPost(pinnedPost);
        final EmojiReactionInfo emojiReaction = pinnedPostEmojiRepository.getEmojiReactionInfoByPostAndMember(pinnedPost, member);

        return PinnedPostDetailInfo.fromEntityWithDomain(pinnedPost, pinnedPost.getAdmin(), pinnedPost.getCategory(), emojiCount, emojiReaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostFileInfo> loadPinnedPostFileList(Long pinnedPostId, String apartCode) {
        // 1. 게시글 조회
        var pinnedPost = pinnedPostRepository.getByIdAndApartCode(pinnedPostId, apartCode);

        // 2. 파일 조회
        final List<PinnedPostFile> postFileList = pinnedPostFileRepository.getAllByPinnedPost(pinnedPost);

        return postFileList.stream()
                .map(PostFileInfo::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentInfo> loadPinnedPostCommentList(Long memberId, String apartCode, Long pinnedPostId, CustomPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 댓글 조회
        var pinnedPostCommentList = pinnedPostCommentRepository.getAllByPinnedPost(pinnedPostId, pageable);
        final List<CommentInfo> commentInfoList = pinnedPostCommentList.stream()
                .map(comment -> CommentInfo.fromEntity(comment, comment.getPinnedPostCommentImages(), comment.getAdmin(), comment.getMember()))
                .toList();

        return new PageImpl<>(commentInfoList, pageable, pinnedPostCommentList.getTotalElements());
    }

    @Override
    @Transactional
    public Long writeComment(Long memberId, Long pinnedPostId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var pinnedPost = pinnedPostRepository.getByIdAndApartCode(pinnedPostId, apartCode);

        // 3. 댓글 저장
        var pinnedPostComment = command.toEntity(pinnedPost, member);
        var newPinnedPostComment = pinnedPostCommentRepository.store(pinnedPostComment);

        // 4. 댓글 이미지 저장
        Optional.ofNullable(image)
                .filter(img -> !img.isEmpty())
                .ifPresent(img -> {
                    UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                    var pinnedPostCommentImage = PinnedPostCommentImage.create(pinnedPostComment, uploadImageInfo.originalImageUrl());
                    pinnedPostCommentImageRepository.store(pinnedPostCommentImage);
                });

        return newPinnedPostComment.getId();
    }

    @Override
    @Transactional
    public Long writeReply(Long memberId, Long pinnedPostId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 및 답글 조회
        Long commentId = Optional.ofNullable(command.getParentId())
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));

        var pinnedPost = pinnedPostRepository.getByIdAndApartCode(pinnedPostId, apartCode);
        var pinnedPostComment = pinnedPostCommentRepository.getByIdAndPinnedPost(commentId, pinnedPost);

        // 3. 답글 저장
        var pinnedPostReply = command.toEntity(pinnedPost, pinnedPostComment, member);
        var newPinnedPostReply = pinnedPostCommentRepository.store(pinnedPostReply);

        // 4. 댓글 이미지 저장
        Optional.ofNullable(image)
                .filter(img -> !img.isEmpty())
                .ifPresent(img -> {
                    UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                    var postCommentImage = PinnedPostCommentImage.create(pinnedPostComment, uploadImageInfo.originalImageUrl());
                    pinnedPostCommentImageRepository.store(postCommentImage);
                });

        return newPinnedPostReply.getId();
    }

    @Override
    @Transactional
    public Long modifyComment(Long memberId, Long pinnedPostId, Long commentId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
        // 1. 댓글 조회
        var pinnedPostComment = pinnedPostCommentRepository.getByIdAndPinnedPostIdAndMemberId(commentId, pinnedPostId, memberId);

        // 2. 댓글 이미지 조회
        var pinnedPostCommentImage = pinnedPostCommentImageRepository.getByPinnedPostCommentId(commentId);

        // 3. 댓글 수정
        pinnedPostComment.modify(command.getContent());

        // 4. 기존 이미지가 있는 경우 기존 이미지 삭제 및 변경
        if (pinnedPostCommentImage != null) {
            pinnedPostCommentImage.delete();
        }

        // 5. 새로운 이미지가 있는 경우 이미지 저장
        Optional.ofNullable(image)
                .filter(img -> !img.isEmpty())
                .ifPresent(img -> {
                    UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                    var newPostCommentImage = PinnedPostCommentImage.create(pinnedPostComment, uploadImageInfo.originalImageUrl());
                    pinnedPostCommentImageRepository.store(newPostCommentImage);
                });

        return pinnedPostComment.getId();
    }

    @Override
    @Transactional
    public EmojiInfo registerEmoji(Long memberId, Long pinnedPostId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var pinnedPost = pinnedPostRepository.getByIdAndApartCode(pinnedPostId, apartCode);

        // 3. 레코드 검사 (이미 등록된 경우 삭제 요청이 필요하다.)
        boolean affected = pinnedPostEmojiRepository.existsByPinnedPostAndMemberAndEmoji(pinnedPost, member);
        if (affected) {
            throw new BaseException(ErrorCode.ALREADY_REGISTER_EMOJI);
        }

        var pinnedPostEmoji = PinnedPostEmoji.create(pinnedPost, member, emoji);
        var newPinnedPostEmojiEmoji = pinnedPostEmojiRepository.store(pinnedPostEmoji);

        return EmojiInfo.fromPinnedPostEmojiEntity(newPinnedPostEmojiEmoji);
    }

    @Override
    @Transactional
    public void deleteEmoji(Long memberId, Long pinnedPostId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var pinnedPost = pinnedPostRepository.getByIdAndApartCode(pinnedPostId, apartCode);

        // 3. 레코드 검사 (등록된 감정 표현이 없을 경우 등록이 필요하다.)
        PinnedPostEmoji pinnedPostEmoji = pinnedPostEmojiRepository.getByPinnedPostAndMemberAndEmoji(pinnedPost, member, emoji);

        // 4. 삭제
        pinnedPostEmojiRepository.delete(pinnedPostEmoji);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PinnedPostSummary> loadPinnedPostList(String apartCode, String categoryCode) {
        List<PinnedPost> pinnedPosts = pinnedPostRepository.getAllByApartCodeAndCategoryCode(apartCode, categoryCode);
        return pinnedPosts.stream()
                .map(pinnedPost -> PinnedPostSummary.fromEntity(pinnedPost, pinnedPost.getAdmin(), pinnedPost.getCategory()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchNoticeInfo> searchPinnedNoticeList(String apartCode, String keyword, String categoryCode) {
        List<PinnedPost> pinnedNoticeList = pinnedPostRepository.getPinnedBulletinListByKeyword(apartCode, keyword, categoryCode);
        return pinnedNoticeList.stream()
            .map(pinnedNotice -> SearchNoticeInfo.fromPinnedNotice(pinnedNotice, pinnedNotice.getAdmin(), pinnedNotice.getCategory()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchDisclosureInfo> searchPinnedDisclosureList(String apartCode, String keyword, String categoryCode) {
        List<PinnedPost> pinnedDisclosureList = pinnedPostRepository.getPinnedBulletinListByKeyword(apartCode, keyword, categoryCode);
        return pinnedDisclosureList.stream()
            .map(pinnedDisclosure -> SearchDisclosureInfo.fromPinnedDisclosure(pinnedDisclosure, pinnedDisclosure.getAdmin(), pinnedDisclosure.getCategory()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchPostInfo> searchPinnedPostList(String apartCode, String keyword, String categoryCode) {
        List<PinnedPost> pinnedPostList = pinnedPostRepository.getPinnedBulletinListByKeyword(apartCode, keyword, categoryCode);
        return pinnedPostList.stream()
            .map(pinnedPost -> SearchPostInfo.fromPinnedPost(pinnedPost, pinnedPost.getAdmin(), pinnedPost.getCategory()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchQnaInfo> searchPinnedQnaList(String apartCode, String keyword, String categoryCode) {
        List<PinnedPost> pinnedQnaList = pinnedPostRepository.getPinnedBulletinListByKeyword(apartCode, keyword, categoryCode);
        return pinnedQnaList.stream()
            .map(pinnedQna -> SearchQnaInfo.fromPinnedQna(pinnedQna, pinnedQna.getAdmin(), pinnedQna.getCategory()))
            .toList();
    }

}
