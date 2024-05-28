package com.fc8.service.impl;

import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.s3.S3Uploader;
import com.fc8.platform.common.utils.ValidateUtils;
import com.fc8.platform.domain.entity.post.PostEmoji;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostEmojiRepository postEmojiRepository;
    private final ApartRepository apartRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public Long writePost(Long memberId, String apartCode, WritePostCommand command, MultipartFile image) {
        // 1. 회원 및 카테고리 조회 (상위 카테고리 : 중요 글, 하위 카테고리 : 본문)
        var member = memberRepository.getActiveMemberById(memberId);
        var category = categoryRepository.getChildCategoryByCode(command.getCategoryCode());
        ValidateUtils.validateChildCategoryType(CategoryType.POST, category);

        // 2. 아파트 정보 조회
        var apart = apartRepository.getByCode(apartCode);

        // 3. 글 저장
        var post = command.toEntity(category, member, apart);
        var newPost = postRepository.store(post);

        // 4. 썸네일 이미지 저장, s3Upload 수정 필요 TODO
        try {
            String url = s3Uploader.uploadFiles(image, "");
            newPost.updateThumbnail(url);
        } catch (IOException e) {
            log.error("썸네일 업로드 실패");
        }

        // 5. 파일 저장 TODO

        return newPost.getId();
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

        // 4. 썸네일 변경 TODO

        return post.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostInfo> loadPostList(Long memberId, String apartCode, SearchPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 게시글 조회 (아파트 코드, 차단 사용자)
        var postList = postRepository.getPostListByApartCode(memberId, apartCode, pageable, command.search());
        final List<PostInfo> postInfoList = postList.stream()
                .map(post -> PostInfo.fromEntity(post, post.getMember(), post.getCategory()))
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

        // 4. 댓글 이미지 저장 TODO

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

        return newPostReply.getId();
    }

    @Override
    @Transactional
    public Long modifyComment(Long memberId, Long postId, Long commentId, String apartCode, WritePostCommentCommand command, MultipartFile image) {
        // 1. 댓글 조회
        var postComment = postCommentRepository.getByIdAndPostIdAndMemberId(commentId, postId, memberId);

        // 2. 댓글 수정
        postComment.modify(command.getContent());

        // 3. 이미지 변경 TODO
        return postComment.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailInfo loadPostDetail(Long memberId, Long postId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var post = postRepository.getPostWithCategoryByIdAndApartCode(postId, apartCode);

        final EmojiCountInfo emojiCount = postEmojiRepository.getEmojiCountInfoByPostAndMember(post);
        final EmojiReactionInfo emojiReaction = postEmojiRepository.getEmojiReactionInfoByPostAndMember(post, member);

        return PostDetailInfo.fromEntity(post, member, post.getCategory(), emojiCount, emojiReaction);
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
}
