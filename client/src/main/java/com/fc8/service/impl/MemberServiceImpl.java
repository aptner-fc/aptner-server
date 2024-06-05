package com.fc8.service.impl;

import com.fc8.external.service.S3UploadService;
import com.fc8.infrastructure.jwt.JwtTokenProvider;
import com.fc8.infrastructure.security.AptnerMember;
import com.fc8.infrastructure.security.CustomUserDetailsService;
import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.CustomRedisException;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.utils.ListUtils;
import com.fc8.platform.common.utils.RedisUtils;
import com.fc8.platform.common.utils.ValidateUtils;
import com.fc8.platform.domain.entity.mapping.ApartMemberMapping;
import com.fc8.platform.domain.entity.mapping.TermsMemberMapping;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.MemberBlock;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostComment;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaComment;
import com.fc8.platform.domain.entity.terms.Terms;
import com.fc8.platform.dto.command.*;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.repository.*;
import com.fc8.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ApartRepository apartRepository;
    private final TermsRepository termsRepository;
    private final MemberRepository memberRepository;
    private final MemberBlockRepository memberBlockRepository;
    private final PostRepository postRepository;
    private final QnaRepository qnaRepository;
    private final PostCommentRepository postCommentRepository;
    private final QnaCommentRepository qnaCommentRepository;
    private final TermsMemberMappingRepository termsMemberMappingRepository;
    private final ApartMemberMappingRepository apartMemberMappingRepository;

    private final S3UploadService s3UploadService;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    private final RedisUtils redisUtils;

    /**
     * 현재 약관 동의 여부가 되지 않은 상태로 들어올 때 회원 엔티티를 영속화하였다가 롤백시키기 때문에 auto increment 증가된다.
     * 문제가 될 것 같으면 mapping 내용을 회원 엔티티에 저장 후 영속화할 필요가 있음.
     */
    @Override
    @Transactional
    public Long signUp(SignUpMemberCommand command) {
        // 1. 휴대전화 인증 검사
        String phone = command.getPhone();
        String verificationCode = command.getVerificationCode();
        validatePhoneAndCode(phone, verificationCode);

        // 2. 유효성 검사 및 회원 정보 생성
        validateDuplication(command);
        var newMember = memberRepository.store(command.toEntity(passwordEncoder.encode(command.getPassword())));

        // 3. 유효성 검사 및 회원 약관 동의 정보 생성
        var termsMemberMappings = getAndValidateTermsWithMapping(newMember, command.getTermsAgreements());
        if (!termsMemberMappings.isEmpty()) {
            termsMemberMappingRepository.storeAll(termsMemberMappings);
        }

        // 4. 회원의 아파트 등록 정보 생성
        createMemberApartInfo(newMember, command);

        return newMember.getId();
    }

    @Override
    @Transactional
    public SignInMemberInfo signIn(SignInMemberCommand command) {
        final String email = command.getEmail();
        final String password = command.getPassword();

        // 1. 회원 조회
        var aptnerMember = (AptnerMember) customUserDetailsService.loadUserByUsername(email);
        var member = aptnerMember.getMember();

        // 2. 비밀번호 검증
        ValidateUtils.validatePassword(password, member.getPassword());

        final MemberInfo memberInfo = MemberInfo.fromEntity(member);
        final TokenInfo tokenInfo = getTokenInfoByEmail(email);

        return new SignInMemberInfo(memberInfo, tokenInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoadMyArticleInfo> loadMyArticleList(Long memberId, String apartCode, CustomPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 3. 작성 게시글 조회
        return memberRepository.getAllArticleByMemberAndApartCode(member, apartCode, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoadMyCommentInfo> loadMyCommentList(Long memberId, String apartCode, CustomPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 3. 작성 댓글 조회
        return memberRepository.getAllCommentByMemberAndApartCode(member, apartCode, pageable);
    }

    @Override
    @Transactional
    public MemberSummary modifyProfile(Long memberId, ModifyProfileCommand command, MultipartFile image) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 닉네임 변경
        member.changeNickname(command.getNickname());

        // 3. 프로필 이미지 변경
        changeProfileImage(member, image);

        return MemberSummary.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberSummary changePassword(Long memberId, ChangePasswordCommand command) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 비밀번호 검증
        String currentPassword = command.getCurrentPassword();
        String newPassword = command.getNewPassword();

        ValidateUtils.validatePassword(currentPassword, member.getPassword());
        ValidateUtils.validateChangePassword(currentPassword, newPassword, command.getConfirmNewPassword());

        // 4. 비밀번호 변경
        member.changePassword(passwordEncoder.encode(newPassword));

        return MemberSummary.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberSummary changePhone(Long memberId, ChangePhoneCommand command) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 휴대전화 인증 검사
        String phone = command.getPhone();
        String verificationCode = command.getVerificationCode();
        validatePhoneAndCode(phone, verificationCode);

        member.changePhone(command.getNewPhone());

        return MemberSummary.fromEntity(member);
    }

    @Override
    @Transactional
    public DeletedCountInfo deleteMyArticleList(Long memberId, String apartCode, DeleteMyArticleListCommand command) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 소통 게시판 글 조회
        var postList = postRepository.getAllByIdsAndMember(command.getPostIds(), member);
        var qnaList = qnaRepository.getAllByIdsAndMember(command.getQnaIds(), member);

        // 3. 게시글 삭제(소통 게시판, QnA 게시판)
        int toBeDeletedCount = getArticleDeletedCount(postList, qnaList);
        if (toBeDeletedCount == 0) {
            return DeletedCountInfo.notDeleted();
        }

        return new DeletedCountInfo(toBeDeletedCount, LocalDateTime.now());
    }

    @Override
    @Transactional
    public DeletedCountInfo deleteMyCommentList(Long memberId, String apartCode, DeleteMyCommentListCommand command) {
        // 1. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 소통 게시판 글 조회
        var postCommentList = postCommentRepository.getAllByIdsAndMember(command.getPostCommentIds(), member);
        var qnaCommentList = qnaCommentRepository.getAllByIdsAndMember(command.getQnaCommentIds(), member);

        // 3. 댓글 삭제(소통 게시판, QnA 게시판)
        int toBeDeletedCount = getCommentDeletedCount(postCommentList, qnaCommentList);
        if (toBeDeletedCount == 0) {
            return DeletedCountInfo.notDeleted();
        }

        return new DeletedCountInfo(toBeDeletedCount, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo findEmail(String apartCode, FindEmailCommand command) {
        var member = memberRepository.getByApartCodeAndNameAndPhone(apartCode, command.getName(), command.getPhone());
        return MemberInfo.fromEntity(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo checkEmail(String apartCode, CheckEmailCommand command) {
        var member = memberRepository.getByApartCodeAndNameAndPhone(apartCode, command.getName(), command.getPhone());
        if (!Objects.equals(member.getEmail(), command.getEmail())) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER);
        }

        return MemberInfo.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberInfo modifyPassword(String apartCode, ModifyPasswordCommand command) {
        // 1. 휴대전화 인증 검사
        String phone = command.getPhone();
        String verificationCode = command.getVerificationCode();
        validatePhoneAndCode(phone, verificationCode);

        // 2. 비밀번호 확인
        String password = command.getPassword();
        ValidateUtils.validateConfirmPassword(password, command.getConfirmPassword());

        var member = memberRepository.getByApartCodeAndEmail(apartCode, command.getEmail());
        member.changePassword(passwordEncoder.encode(password));
        return MemberInfo.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberInfo blockMember(Long memberId, String apartCode, BlockMemberCommand command) {
        // 1. 유효성 검사
        Long blockedMemberId = command.getBlockedMemberId();
        checkBlockOrUnBlockMySelf(memberId, blockedMemberId);

        // 2. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 3. 차단 회원 조회
        var blockedMember = memberRepository.getByIdAndApartCode(blockedMemberId, apartCode);

        // 4. 차단 여부 조회
        boolean affected = memberBlockRepository.existsByMemberAndBlocked(member, blockedMember);
        if (affected) {
            throw new InvalidParamException(ErrorCode.ALREADY_BLOCK);
        }

        // 5. 차단
        var memberBlock = MemberBlock.block(member, blockedMember);
        memberBlockRepository.store(memberBlock);

        return MemberInfo.fromEntity(blockedMember);
    }

    @Override
    @Transactional
    public MemberInfo unBlockMember(Long memberId, String apartCode, UnBlockMemberCommand command) {
        // 1. 유효성 검사
        Long blockedMemberId = command.getUnBlockedMemberId();
        checkBlockOrUnBlockMySelf(memberId, blockedMemberId);

        // 2. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 3. 차단을 해제할 회원 조회
        var unBlockedMember = memberRepository.getByIdAndApartCode(blockedMemberId, apartCode);

        // 4. 차단 여부 조회
        boolean affected = memberBlockRepository.existsByMemberAndBlocked(member, unBlockedMember);
        if (!affected) {
            throw new InvalidParamException(ErrorCode.ALREADY_UNBLOCK);
        }

        // 5. 차단 해제
        MemberBlock memberBlock = memberBlockRepository.getByMemberAndBlocked(member, unBlockedMember);
        memberBlockRepository.delete(memberBlock);

        return MemberInfo.fromEntity(unBlockedMember);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberSummary> loadBlockedMember(Long memberId, String apartCode, CustomPageCommand command) {
        // 1. 페이지 생성
        Pageable pageable = PageRequest.of(command.page() - 1, command.size());

        // 2. 회원 조회
        var member = memberRepository.getActiveMemberById(memberId);

        // 3. 차단 회원 리스트 조회
        return memberRepository.getAllBlockedMemberByMemberAndApartCode(member, apartCode, pageable);
    }

    private void checkBlockOrUnBlockMySelf(Long memberId, Long blockedMemberId) {
        if (Objects.equals(memberId, blockedMemberId)) {
            throw new InvalidParamException(ErrorCode.CAN_NOT_BLOCK);
        }
    }

    private int getArticleDeletedCount(List<Post> postList, List<Qna> qnaList) {
        int toBeDeletedCount = postList.size() + qnaList.size();
        if (toBeDeletedCount == 0) {
            return 0;
        }

        try {
            postList.forEach(Post::delete);
            qnaList.forEach(Qna::delete);
        } catch (InvalidParamException e) {
            toBeDeletedCount = toBeDeletedCount - 1;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return toBeDeletedCount;
    }

    private int getCommentDeletedCount(List<PostComment> postCommentList, List<QnaComment> qnaCommentList) {
        int toBeDeletedCount = postCommentList.size() + qnaCommentList.size();
        if (toBeDeletedCount == 0) {
            return 0;
        }

        try {
            postCommentList.forEach(PostComment::delete);
            qnaCommentList.forEach(QnaComment::delete);
        } catch (InvalidParamException e) {
            toBeDeletedCount = toBeDeletedCount - 1;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return toBeDeletedCount;
    }

    private void validatePhoneAndCode(String phone, String verificationCode) {
        if (memberRepository.existPhone(phone)) {
            throw new InvalidParamException(ErrorCode.EXIST_PHONE);
        }

        if (!redisUtils.isValidateAndVerified(phone, verificationCode)) {
            throw new CustomRedisException(ErrorCode.INVALID_OR_EXPIRED_SMS);
        }
    }

    private void changeProfileImage(Member member, MultipartFile image) {
        Optional.ofNullable(image)
                .filter(img -> !img.isEmpty())
                .ifPresent(img -> {
                    UploadImageInfo uploadImageInfo = s3UploadService.uploadPostImage(image);
                    member.changeProfileImage(uploadImageInfo.originalImageUrl());
                });
    }

    private void createMemberApartInfo(Member newMember, SignUpMemberCommand command) {
        final ApartmentInfo apartment = command.getApartment();
        final ApartDetailInfo apartDetailInfo = apartment.apartDetailInfo();

        var apart = apartRepository.getByCode(apartment.code());
        var apartMemberMapping = ApartMemberMapping.createFirst(apart, newMember, apartDetailInfo.toDomain());

        apartMemberMappingRepository.store(apartMemberMapping);
    }

    private TokenInfo getTokenInfoByEmail(String email) {
        final String accessToken = jwtTokenProvider.createAccessToken(email);
        final Date accessExpiredAt = jwtTokenProvider.getExpirationByToken(accessToken);
        return new TokenInfo(accessToken, accessExpiredAt);
    }

    private List<TermsMemberMapping> getAndValidateTermsWithMapping(Member member, List<TermsAgreement> termsAgreements) {
        var usedTermsList = termsRepository.getAllByIsUsed();
        // 1. 사용중인 약관이 존재하지 않을 경우 생성하지 않는다.
        if (!usedTermsList.isEmpty()) {
            validateTermsList(usedTermsList, termsAgreements);
        }

        return getTermsMemberMappings(member, usedTermsList, termsAgreements);
    }

    private void validateTermsList(List<Terms> usedTermsList, List<TermsAgreement> termsAgreements) {
        if (!ListUtils.isEqualSize(usedTermsList, termsAgreements)) {
            throw new InvalidParamException(ErrorCode.MISMATCH_TERMS_AGREEMENT);
        }
    }

    private List<TermsMemberMapping> getTermsMemberMappings(Member member, List<Terms> usedTermsList, List<TermsAgreement> termsAgreements) {
        List<TermsMemberMapping> mappings = new ArrayList<>();
        final LocalDateTime now = LocalDateTime.now();

        usedTermsList.forEach(terms -> {
            TermsAgreement termsAgreement = termsAgreements.stream()
                    .filter(agreement -> agreement.termsId().equals(terms.getId()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TERMS));

            final boolean isAgreed = termsAgreement.isAgreed();

            if (terms.isRequired() && !isAgreed) {
                throw new InvalidParamException(ErrorCode.MISSING_REQUIRED_AGREEMENT);
            }

            mappings.add(TermsMemberMapping.create(member, terms, isAgreed, now));
        });

        return mappings;
    }

    private void validateDuplication(SignUpMemberCommand command) {
        validateDuplicatedEmail(command.getEmail());
        validateDuplicatedNickname(command.getNickname());
    }

    private void validateDuplicatedEmail(String email) {
        if (memberRepository.existActiveEmail(email)) {
            throw new InvalidParamException(ErrorCode.EXIST_EMAIL);
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        if (memberRepository.existNickname(nickname)) {
            throw new InvalidParamException(ErrorCode.EXIST_NICKNAME);
        }
    }
}
