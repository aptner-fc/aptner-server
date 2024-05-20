package com.fc8.service.impl;

import com.fc8.infrastructure.jwt.JwtTokenProvider;
import com.fc8.infrastructure.security.AptnerAdmin;
import com.fc8.infrastructure.security.CustomUserDetailsService;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.utils.ValidateUtils;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.MemberAuth;
import com.fc8.platform.dto.command.SignInAdminCommand;
import com.fc8.platform.dto.command.SignUpAdminCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.repository.AdminRepository;
import com.fc8.platform.repository.ApartRepository;
import com.fc8.platform.repository.MemberAuthRepository;
import com.fc8.platform.repository.MemberRepository;
import com.fc8.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ApartRepository apartRepository;
    private final MemberRepository memberRepository;
    private final MemberAuthRepository memberAuthRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    @Transactional
    public Long signUp(SignUpAdminCommand command) {
        validateAdminCommand(command);
        var apart = apartRepository.getByCode(command.getApartCode());
        var newAdmin = adminRepository.store(command.toEntity(passwordEncoder.encode(command.getPassword()), apart));
        return newAdmin.getId();
    }

    @Override
    @Transactional
    public SignInAdminInfo signIn(SignInAdminCommand command) {
        String email = command.getEmail();
        String password = command.getPassword();

        // 1. 회원 조회
        AptnerAdmin aptnerAdmin = (AptnerAdmin) customUserDetailsService.loadUserByUsername(email);
        Admin admin = aptnerAdmin.getAptnerAdmin();

        // 2. 비밀번호 검증
        ValidateUtils.validatePassword(password, admin.getPassword());

        final AdminInfo adminInfo = AdminInfo.fromEntity(admin);
        final TokenInfo tokenInfo = getTokenInfoByEmail(email);

        return new SignInAdminInfo(adminInfo, tokenInfo);
    }

    @Override
    @Transactional
    public AuthMemberInfo authenticateMember(Long adminId, Long memberId, ApartInfo apartInfo) {
        var admin = adminRepository.getById(adminId);
        var member = memberRepository.getActiveMemberById(memberId);

        member.updateActive();

        Member storedMember = memberRepository.store(member);

        MemberAuth memberAuth = MemberAuth.of(storedMember, admin);
        MemberAuth newMemberAuth = memberAuthRepository.store(memberAuth);

        return new AuthMemberInfo(
                newMemberAuth.getId(),
                MemberInfo.fromEntity(storedMember),
                AdminInfo.fromEntity(admin),
                apartInfo,
                memberAuth.getAuthenticatedAt());
    }

    private TokenInfo getTokenInfoByEmail(String email) {
        String accessToken = jwtTokenProvider.createAccessToken(email);
        Date accessExpiredAt = jwtTokenProvider.getExpirationByToken(accessToken);
        return new TokenInfo(accessToken, accessExpiredAt);
    }

    private void validateAdminCommand(SignUpAdminCommand command) {
        validateDuplicatedEmail(command.getEmail());
        validateDuplicatedNickname(command.getNickname());
    }

    private void validateDuplicatedEmail(String email) {
        if (adminRepository.existActiveEmail(email)) {
            throw new InvalidParamException(ErrorCode.EXIST_EMAIL);
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        if (adminRepository.existNickname(nickname)) {
            throw new InvalidParamException(ErrorCode.EXIST_NICKNAME);
        }
    }
}
