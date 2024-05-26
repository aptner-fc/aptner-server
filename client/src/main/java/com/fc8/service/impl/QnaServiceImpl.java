package com.fc8.service.impl;

import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.s3.S3Uploader;
import com.fc8.platform.domain.entity.qna.QnaEmoji;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteQnaCommand;
import com.fc8.platform.dto.command.WriteQnaCommentCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.repository.*;
import com.fc8.platform.repository.QnaRepository;
import com.fc8.service.QnaService;
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
public class QnaServiceImpl implements QnaService {

    private final QnaRepository qnaRepository;
    private final QnaCommentRepository qnaCommentRepository;
    private final ApartRepository apartRepository;
    private final QnaEmojiRepository qnaEmojiRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public Long create(Long memberId, String apartCode, WriteQnaCommand command, MultipartFile image) {
        // 1. 카테고리 및 회원 검사 (상위 카테고리 : 중요 글, 하위 카테고리 : 본문)
        var category = categoryRepository.getChildCategoryByCode(command.getCategoryCode());
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 아파트 정보 조회
        var apart = apartRepository.getByCode(apartCode);

        // 3. 글 저장
        var qna = command.toEntity(category, member, apart);
        var srotedQna = qnaRepository.store(qna);

        // 4. 썸네일 이미지 저장, s3Upload 수정 필요 TODO
        try {
            String url = s3Uploader.uploadFiles(image, "");
//            storedPost.updateThumbnail(url);
        } catch (IOException e) {
            log.error("썸네일 업로드 실패");
        }

        // 5. 파일 저장 TODO
        return srotedQna.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QnaInfo> loadQnaList(Long memberId, String apartCode, SearchPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 게시글 조회 (아파트 코드, 차단 사용자)
        var qnaList = qnaRepository.getQnaListByApartCode(memberId, apartCode, pageable, command.search());
        List<QnaInfo> qnaInfoList = qnaList.stream()
            .map(qna -> QnaInfo.fromEntity(qna, qna.getMember(), qna.getCategory()))
            .toList();

        return new PageImpl<>(qnaInfoList, pageable, qnaList.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteQna(Long memberId, Long qnaId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var qna = qnaRepository.getQnaWithCategoryByIdAndApartCode(qnaId, apartCode);

        // 3. 삭제
        qnaRepository.delete(qna);
    }

    @Override
    @Transactional(readOnly = true)
    public QnaDetailInfo loadQnaDetail(Long memberId, Long qnaId, String apartCode) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var qna = qnaRepository.getQnaWithCategoryByIdAndApartCode(qnaId, apartCode);

        final EmojiCountInfo emojiCount = qnaEmojiRepository.getEmojiCountInfoByQnaAndMember(qna);
        final EmojiReactionInfo emojiReaction = qnaEmojiRepository.getEmojiReactionInfoByQnaAndMember(qna, member);

        return QnaDetailInfo.fromEntity(qna, member, qna.getCategory(), emojiCount, emojiReaction);
    }

    @Override
    @Transactional
    public EmojiInfo registerEmoji(Long memberId, Long qnaId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var qna = qnaRepository.getQnaWithCategoryByIdAndApartCode(qnaId, apartCode);

        // 3. 레코드 검사 (이미 등록된 경우 삭제 요청이 필요하다.)
        boolean affected = qnaEmojiRepository.existsByQnaAndMemberAndEmoji(qna, member, emoji);
        if (affected) {
            throw new BaseException(ErrorCode.ALREADY_REGISTER_EMOJI);
        }

        var qnaEmoji = QnaEmoji.create(qna, member, emoji);
        var newQnaEmoji = qnaEmojiRepository.store(qnaEmoji);

        return EmojiInfo.fromQnaEmojiEntity(newQnaEmoji);
    }

    @Override
    @Transactional
    public void deleteEmoji(Long memberId, Long qnaId, String apartCode, EmojiType emoji) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var qna = qnaRepository.getQnaWithCategoryByIdAndApartCode(qnaId, apartCode);

        // 3. 레코드 검사 (등록된 감정 표현이 없을 경우 등록이 필요하다.)
        QnaEmoji qnaEmoji = qnaEmojiRepository.getByQnaAndMemberAndEmoji(qna, member, emoji);

        // 4. 삭제
        qnaEmojiRepository.delete(qnaEmoji);
    }

    @Override
    @Transactional
    public Long writeReply(Long memberId, Long qnaId, String apartCode, WriteQnaCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 및 답글 조회
        var qna = qnaRepository.getByIdAndApartCode(qnaId, apartCode);
        var qnaComment = qnaCommentRepository.getByIdAndQna(Optional.ofNullable(command.getParentId())
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT)), qna);

        // 3. 답글 저장
        var qnaReply = command.toEntity(qna, qnaComment, member);
        var newQnaReply = qnaCommentRepository.store(qnaReply);

        return newQnaReply.getId();
    }

    @Override
    @Transactional
    public Long writeComment(Long memberId, Long qnaId, String apartCode, WriteQnaCommentCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 게시글 조회
        var qna = qnaRepository.getByIdAndApartCode(qnaId, apartCode);

        // 3. 댓글 저장
        var qnaComment = command.toEntity(qna, member);
        var newQnaComment = qnaCommentRepository.store(qnaComment);


        // 4. 댓글 이미지 저장 TODO

        return newQnaComment.getId();
    }
}
