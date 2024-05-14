package com.fc8.aptner.core.service.impl;

import com.fc8.aptner.common.exception.InvalidParamException;
import com.fc8.aptner.common.exception.code.ErrorCode;
import com.fc8.aptner.common.utils.ListUtils;
import com.fc8.aptner.core.domain.entity.mapping.TermsMemberMapping;
import com.fc8.aptner.core.domain.entity.member.Member;
import com.fc8.aptner.core.domain.entity.terms.Terms;
import com.fc8.aptner.core.dto.MemberInfo;
import com.fc8.aptner.core.dto.SignInMemberInfo;
import com.fc8.aptner.core.dto.TermsAgreement;
import com.fc8.aptner.core.dto.TokenInfo;
import com.fc8.aptner.core.dto.command.SignInMemberCommand;
import com.fc8.aptner.core.dto.command.SignUpMemberCommand;
import com.fc8.aptner.core.repository.MemberRepository;
import com.fc8.aptner.core.repository.TermsMemberMappingRepository;
import com.fc8.aptner.core.repository.TermsRepository;
import com.fc8.aptner.core.service.MemberService;
import com.fc8.aptner.infrastructure.jwt.JwtTokenProvider;
import com.fc8.aptner.security.AptnerMember;
import com.fc8.aptner.security.CustomUserDetailsService;
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

    private final MemberRepository memberRepository;
    private final TermsRepository termsRepository;
    private final TermsMemberMappingRepository termsMemberMappingRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    @Transactional
    public Long signUp(SignUpMemberCommand command) {
        // 1. 유효성 검사 및 회원 정보 생성
        validateDuplicatedEmail(command.getEmail());
        validateDuplicatedNickname(command.getNickname());
        var newMember = memberRepository.store(command.toEntity(passwordEncoder.encode(command.getPassword())));

        // 2. 유효성 검사 및 회원 약관 동의 정보 생성
        List<TermsMemberMapping> termsMemberMappings = getAndValidateTermsWithMapping(newMember, command.getTermsAgreements());
        if (!termsMemberMappings.isEmpty()) {
            termsMemberMappingRepository.storeAll(termsMemberMappings);
        }

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
        validatePassword(password, member.getPassword());

        final MemberInfo memberInfo = MemberInfo.fromEntity(member);
        final TokenInfo tokenInfo = getTokenInfoByEmail(email);

        return new SignInMemberInfo(memberInfo, tokenInfo);
    }

    private TokenInfo getTokenInfoByEmail(String email) {
        String accessToken = jwtTokenProvider.createAccessToken(email);
        Date accessExpiredAt = jwtTokenProvider.getExpirationByToken(accessToken);
        return new TokenInfo(accessToken, accessExpiredAt);
    }

    private void validatePassword(String password, String encPassword) {
        if (!passwordEncoder.matches(password, encPassword)) {
            throw new InvalidParamException(ErrorCode.FAIL_LOGIN);
        }
    }

    private List<TermsMemberMapping> getAndValidateTermsWithMapping(Member member, List<TermsAgreement> termsAgreements) {
        List<Terms> usedTermsList = termsRepository.getAllByIsUsed();
        // 사용중인 약관이 존재하지 않을 경우 생성하지 않는다.
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
