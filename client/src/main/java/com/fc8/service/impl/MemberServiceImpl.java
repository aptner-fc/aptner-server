package com.fc8.service.impl;

import com.fc8.infrastructure.jwt.JwtTokenProvider;
import com.fc8.infrastructure.security.AptnerMember;
import com.fc8.infrastructure.security.CustomUserDetailsService;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.utils.*;
import com.fc8.platform.domain.entity.mapping.ApartMemberMapping;
import com.fc8.platform.domain.entity.mapping.TermsMemberMapping;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.terms.Terms;
import com.fc8.platform.dto.command.SignInMemberCommand;
import com.fc8.platform.dto.command.SignUpMemberCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.repository.*;
import com.fc8.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ApartRepository apartRepository;
    private final TermsRepository termsRepository;
    private final MemberRepository memberRepository;
    private final TermsMemberMappingRepository termsMemberMappingRepository;
    private final ApartMemberMappingRepository apartMemberMappingRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    private final SMSUtils smsUtils;
    private final RedisUtils redisUtils;

    /**
     * 현재 약관 동의 여부가 되지 않은 상태로 들어올 때 회원 엔티티를 영속화하였다가 롤백시키기 때문에 auto increment 증가된다.
     * 문제가 될 것 같으면 mapping 내용을 회원 엔티티에 저장 후 영속화할 필요가 있음.
     */
    @Override
    @Transactional
    public Long signUp(SignUpMemberCommand command) {
        // 1. 유효성 검사 및 회원 정보 생성
        validateDuplication(command);
        var newMember = memberRepository.store(command.toEntity(passwordEncoder.encode(command.getPassword())));

        // 2. 유효성 검사 및 회원 약관 동의 정보 생성
        List<TermsMemberMapping> termsMemberMappings = getAndValidateTermsWithMapping(newMember, command.getTermsAgreements());
        if (!termsMemberMappings.isEmpty()) {
            termsMemberMappingRepository.storeAll(termsMemberMappings);
        }

        // 3. 회원의 아파트 등록 정보 생성
        createMemberApartInfo(newMember, command);

        return newMember.getId();
    }

    @Override
    @Transactional
    public SignInMemberInfo signIn(SignInMemberCommand command) {
        String email = command.getEmail();
        String password = command.getPassword();

        // 1. 회원 조회
        AptnerMember aptnerMember = (AptnerMember) customUserDetailsService.loadUserByUsername(email);
        Member member = aptnerMember.getMember();

        // 2. 비밀번호 검증
        ValidateUtils.validatePassword(password, member.getPassword());

        final MemberInfo memberInfo = MemberInfo.fromEntity(member);
        final TokenInfo tokenInfo = getTokenInfoByEmail(email);

        return new SignInMemberInfo(memberInfo, tokenInfo);
    }

    private void createMemberApartInfo(Member newMember, SignUpMemberCommand command) {
        ApartmentInfo apartment = command.getApartment();
        ApartDetailInfo apartDetailInfo = apartment.apartDetailInfo();

        var apart = apartRepository.getByCode(apartment.code());
        var apartMemberMapping = ApartMemberMapping.createFirst(apart, newMember, apartDetailInfo.toDomain());

        apartMemberMappingRepository.store(apartMemberMapping);
    }

    private TokenInfo getTokenInfoByEmail(String email) {
        String accessToken = jwtTokenProvider.createAccessToken(email);
        Date accessExpiredAt = jwtTokenProvider.getExpirationByToken(accessToken);
        return new TokenInfo(accessToken, accessExpiredAt);
    }

    private List<TermsMemberMapping> getAndValidateTermsWithMapping(Member member, List<TermsAgreement> termsAgreements) {
        final List<Terms> usedTermsList = termsRepository.getAllByIsUsed();
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
        LocalDateTime now = LocalDateTime.now();

        usedTermsList.forEach(terms -> {
            TermsAgreement termsAgreement = termsAgreements.stream()
                    .filter(agreement -> agreement.termsId().equals(terms.getId()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TERMS));

            boolean isAgreed = termsAgreement.isAgreed();

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
