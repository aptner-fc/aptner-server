package com.fc8.facade;

import com.fc8.platform.domain.enums.ProcessingStatus;
import com.fc8.platform.dto.command.SignInAdminCommand;
import com.fc8.platform.dto.command.SignUpAdminCommand;
import com.fc8.platform.dto.command.WriteQnaAnswerCommand;
import com.fc8.platform.dto.record.ApartInfo;
import com.fc8.platform.dto.record.AuthMemberInfo;
import com.fc8.platform.dto.record.SignInAdminInfo;
import com.fc8.platform.dto.response.SignInAdminResponse;
import com.fc8.platform.dto.response.SignUpAdminResponse;
import com.fc8.platform.dto.response.AuthMemberResponse;
import com.fc8.platform.dto.response.WriteQnaAnswerResponse;
import com.fc8.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminFacade {

    private final AdminService adminService;

    public SignUpAdminResponse signUp(SignUpAdminCommand command) {
        return new SignUpAdminResponse(adminService.signUp(command));
    }

    public SignInAdminResponse signIn(SignInAdminCommand command) {
        SignInAdminInfo signInAdminInfo = adminService.signIn(command);
        return new SignInAdminResponse(new SignInAdminResponse.SignInAdmin(signInAdminInfo.adminInfo()), signInAdminInfo.tokenInfo());
    }

    public AuthMemberResponse authenticateMember(Long adminId, Long memberId, ApartInfo apartInfo) {
        AuthMemberInfo authMemberInfo = adminService.authenticateMember(adminId, memberId, apartInfo);
        return new AuthMemberResponse(
                new AuthMemberResponse.AuthMember(authMemberInfo.memberInfo()),
                new AuthMemberResponse.AuthAdmin(authMemberInfo.adminInfo()),
                authMemberInfo.apartInfo(),
                authMemberInfo.authenticatedAt());
    }

    @Transactional
    public WriteQnaAnswerResponse writeAnswer(Long adminId, Long qnaId, String apartCode, WriteQnaAnswerCommand command) {
        adminService.changeStatus(adminId, qnaId, apartCode, command.getProcessingStatus());
        return new WriteQnaAnswerResponse(adminService.writeAnswer(adminId, qnaId, apartCode, command));
    }

    public void changeStatus(Long adminId, Long qnaId, String apartCode, ProcessingStatus status) {
        adminService.changeStatus(adminId, qnaId, apartCode, status);
    }
}
