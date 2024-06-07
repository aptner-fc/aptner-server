package com.fc8.service.impl;

import com.fc8.external.service.S3UploadService;
import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.properties.AptnerProperties;
import com.fc8.platform.common.utils.FileUtils;
import com.fc8.platform.common.utils.ValidateUtils;
import com.fc8.platform.domain.entity.apartment.ApartArea;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.mapping.ApartAreaPostMapping;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostCommentImage;
import com.fc8.platform.domain.entity.post.PostEmoji;
import com.fc8.platform.domain.entity.post.PostFile;
import com.fc8.platform.domain.enums.CategoryType;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WritePostCommand;
import com.fc8.platform.dto.command.WritePostCommentCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.repository.*;
import com.fc8.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostEmojiRepository postEmojiRepository;
    private final PostFileRepository postFileRepository;
    private final ApartRepository apartRepository;
    private final ApartAreaRepository apartAreaRepository;
    private final ApartAreaPostMappingRepository apartAreaPostMappingRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PostCommentImageRepository postCommentImageRepository;
    private final S3UploadService s3UploadService;

    @Override
    @Transactional
    public Long writePost(Long memberId, String apartCode, WritePostCommand command, MultipartFile image, List<MultipartFile> files) {
        // 1. 회원 및 카테고리 조회 (상위 카테고리 : 중요 글, 하위 카테고리 : 본문)
        var member = memberRepository.getActiveMemberById(memberId);
        var category = categoryRepository.getChildCategoryByCode(command.getCategoryCode());
        ValidateUtils.validateChildCategoryType(CategoryType.POST, category);

        // 2. 아파트 정보 조회
        var apart = apartRepository.getByCode(apartCode);

        // 3-1. 글 저장
        var post = command.toEntity(category, member, apart);
        postRepository.store(post);

        // 3-2. 인테리어 게시판일 경우, 추가 정보 저장
        Long apartAreaId = command.getApartAreaId();
        updateApartArea(apartAreaId, post, category);

        // 4. 썸네일 이미지 저장
        uploadPostThumbnailImage(post, image);

        // 5. 파일 저장
        List<PostFile> newPostFileList = uploadPostFileList(post, files);
        postFileRepository.storeAll(newPostFileList);

        return post.getId();
    }

    private void updateApartArea(Long apartAreaId, Post post, Category category) {
        Optional.ofNullable(apartAreaId)
                .map(apartAreaRepository::getById)
                .ifPresent(apartArea -> {
                    ValidateUtils.validateInteriorPost(category);

                    ApartAreaPostMapping apartAreaPostMapping =
                            apartAreaPostMappingRepository.findByPost(post)
                                    .orElse(ApartAreaPostMapping.create(apartArea, post));

                    if (apartAreaPostMappingRepository.existByPost(post)) {
                        apartAreaPostMapping.changeApartArea(apartArea);
                    }

                    apartAreaPostMappingRepository.store(apartAreaPostMapping);
                });
    }

    private List<PostFile> uploadPostFileList(Post post, List<MultipartFile> files) {
        List<PostFile> newPostFileList = new ArrayList<>();
        Optional.ofNullable(files)
                .filter(f -> !f.isEmpty())
                .ifPresent(nonEmptyFiles -> {
                    FileUtils.validateFiles(nonEmptyFiles);
                    if (nonEmptyFiles.size() > AptnerProperties.FILE_MAX_SIZE_COUNT) {
                        throw new InvalidParamException(ErrorCode.EXCEEDED_FILE_COUNT);
                    }

                    nonEmptyFiles.forEach(file -> {
                        UploadFileInfo uploadFileInfo = s3UploadService.uploadPostFile(file);
                        var newPostFile = PostFile.create(post, uploadFileInfo.originalFileName(), uploadFileInfo.originalFilUrl(), uploadFileInfo.fileExtension(), uploadFileInfo.fileSize());
                        newPostFileList.add(newPostFile);
                    });
                });

        return newPostFileList;
    }

    @Override
    @Transactional
    public Long modifyPost(Long memberId, Long postId, String apartCode, WritePostCommand command, MultipartFile image) {
        // 1. 게시글 조회
        var post = postRepository.getByIdAndMemberId(postId, memberId);

        // 2. 요청 값 조회
        var category = categoryRepository.getChildCategoryByCode(command.getCategoryCode());
        ValidateUtils.validateChildCategoryType(CategoryType.POST, category);

        // 3. 게시글 수정
        post.changeCategory(category);
        post.modify(command.getTitle(), command.getContent());

        Long apartAreaId = command.getApartAreaId();
        updateApartArea(apartAreaId, post, category);

        return post.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostSummary> loadPostList(Long memberId, String apartCode, SearchPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 게시글 조회 (아파트 코드, 차단 사용자)
        var postList = postRepository.getPostListByApartCode(memberId, apartCode, pageable, command.search(), command.type(), command.categoryCode());
        final List<PostSummary> postInfoList = postList.stream()
                .map(post -> PostSummary.fromEntity(post, post.getMember(), post.getCategory()))
                .toList();

        return new PageImpl<>(postInfoList, pageable, postList.getTotalElements());
    }

    @Override
    @Transactional
    public Long writeComment(Long memberId, Long postId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var post = postRepository.getByIdAndApartCode(postId, apartCode);

        // 3. 댓글 저장
        var postComment = command.toEntity(post, member);
        var newPostComment = postCommentRepository.store(postComment);

        // 4. 댓글 이미지 저장
        Optional.ofNullable(image)
            .filter(img -> !img.isEmpty())
            .ifPresent(img -> {
                UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                var postCommentImage = PostCommentImage.create(postComment, uploadImageInfo.originalImageUrl());
                postCommentImageRepository.store(postCommentImage);
            });

        return newPostComment.getId();
    }

    @Override
    @Transactional
    public Long writeReply(Long memberId, Long postId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 및 답글 조회
        Long commentId = Optional.ofNullable(command.getParentId())
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));

        var post = postRepository.getByIdAndApartCode(postId, apartCode);
        var postComment = postCommentRepository.getByIdAndPost(commentId, post);

        // 3. 답글 저장
        var postReply = command.toEntity(post, postComment, member);
        var newPostReply = postCommentRepository.store(postReply);

        // 4. 댓글 이미지 저장
        Optional.ofNullable(image)
            .filter(img -> !img.isEmpty())
            .ifPresent(img -> {
                UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                var postCommentImage = PostCommentImage.create(postComment, uploadImageInfo.originalImageUrl());
                postCommentImageRepository.store(postCommentImage);
            });

        return newPostReply.getId();
    }

    @Override
    @Transactional
    public Long modifyComment(Long memberId, Long postId, Long commentId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
        // 1. 댓글 조회
        var postComment = postCommentRepository.getByIdAndPostIdAndMemberId(commentId, postId, memberId);

        // 2. 댓글 이미지 조회
        var postCommentImage = postCommentImageRepository.getImageByQnaCommentId(commentId);

        // 3. 댓글 수정
        postComment.modify(command.getContent());

        // 4. 기존 이미지 삭제 및 변경
        postCommentImage.delete();

        Optional.ofNullable(image)
            .filter(img -> !img.isEmpty())
            .ifPresent(img -> {
                UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);

                var newPostCommentImage = PostCommentImage.create(postComment, uploadImageInfo.originalImageUrl());
                postCommentImageRepository.store(newPostCommentImage);
            });

        return postComment.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailInfo loadPostDetail(Long memberId, Long postId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var post = postRepository.getPostWithCategoryByIdAndApartCode(memberId, postId, apartCode);

        // 3. 인테리어 정보 조회
        var apartArea = apartAreaPostMappingRepository.findByPost(post)
                .map(ApartAreaPostMapping::getApartArea)
                .orElse(null);

        final EmojiCountInfo emojiCount = postEmojiRepository.getEmojiCountInfoByPostAndMember(post);
        final EmojiReactionInfo emojiReaction = postEmojiRepository.getEmojiReactionInfoByPostAndMember(post, member);

        return PostDetailInfo.fromEntityWithDomain(post, post.getMember(), post.getCategory(), apartArea, emojiCount, emojiReaction);
    }

    @Override
    @Transactional
    public EmojiInfo registerEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var post = postRepository.getByIdAndApartCode(postId, apartCode);

        // 3. 레코드 검사 (이미 등록된 경우 삭제 요청이 필요하다.)
        boolean affected = postEmojiRepository.existsByPostAndMemberAndEmoji(post, member, emoji);
        if (affected) {
            throw new BaseException(ErrorCode.ALREADY_REGISTER_EMOJI);
        }

        var postEmoji = PostEmoji.create(post, member, emoji);
        var newPostEmoji = postEmojiRepository.store(postEmoji);

        return EmojiInfo.fromPostEmojiEntity(newPostEmoji);
    }

    @Override
    @Transactional
    public Long deletePost(Long memberId, Long postId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회 (본인 게시글 여부)
        var post = postRepository.getByIdAndApartCode(postId, apartCode);

        // 3. 본인 작성글 여부
        boolean affected = postRepository.isWriter(post, member);
        if (!affected) {
            throw new InvalidParamException(ErrorCode.NOT_POST_WRITER);
        }

        // 4. 삭제
        post.delete();

        return postId;
    }

    @Override
    @Transactional
    public void deleteEmoji(Long memberId, Long postId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var post = postRepository.getByIdAndApartCode(postId, apartCode);

        // 3. 레코드 검사 (등록된 감정 표현이 없을 경우 등록이 필요하다.)
        PostEmoji postEmoji = postEmojiRepository.getByPostAndMemberAndEmoji(post, member, emoji);

        // 4. 삭제
        postEmojiRepository.delete(postEmoji);
    }

    @Override
    @Transactional
    public Long deleteComment(Long memberId, Long postId, Long commentId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var post = postRepository.getByIdAndApartCode(postId, apartCode);

        // 3. 댓글 조회
        var comment = postCommentRepository.getByIdAndPost(commentId, post);

        // 3. 본인 작성 댓글 여부
        boolean affected = postCommentRepository.isWriter(comment, member);
        if (!affected) {
            throw new InvalidParamException(ErrorCode.NOT_POST_COMMENT_WRITER);
        }

        // 4. 삭제
        comment.delete();

        return commentId;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostCommentInfo> loadCommentList(Long memberId, String apartCode, Long postId, CustomPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 댓글 조회
        return postCommentRepository.getCommentListByPost(postId, pageable);
    }

    @Override
    public List<PostFileInfo> loadPostFileList(Long postId, String apartCode) {
        // 게시글 조회
        var post = postRepository.getByIdAndApartCode(postId, apartCode);

        // 파일 조회
        final List<PostFile> postFileList = postFileRepository.getPostFileListByPost(post);

        return postFileList.stream().map(PostFileInfo::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApartAreaSummary> loadApartArea(String apartCode) {
        List<ApartArea> apartAreaList = apartAreaRepository.getAllByApartCode(apartCode);
        return apartAreaList.stream()
                .map(apartArea -> ApartAreaSummary.fromEntity(apartArea))
                .toList();
    }

    private void uploadPostThumbnailImage(Post post, MultipartFile image) {
        if (null == image || image.isEmpty()) {
            return;
        }

        UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);
        post.updateThumbnail(uploadImageInfo.originalImageUrl());

//        Optional.ofNullable(image)
//                .filter(img -> !img.isEmpty())
//                .ifPresent(img -> {
//                    // 이미지 용량 제한 TODO
//                    UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);
//                    post.updateThumbnail(uploadImageInfo.originalImageUrl());
//                });
    }
}
