package com.fc8.service;

import com.fc8.platform.domain.enums.ProcessingStatus;
import com.fc8.platform.dto.command.WriteQnaAnswerCommand;
import com.fc8.platform.dto.record.ApartInfo;
import com.fc8.platform.dto.record.AuthMemberInfo;
import com.fc8.platform.dto.record.SignInAdminInfo;
import com.fc8.platform.dto.command.SignInAdminCommand;
import com.fc8.platform.dto.command.SignUpAdminCommand;

public interface AdminService {

    Long signUp(SignUpAdminCommand command);

    SignInAdminInfo signIn(SignInAdminCommand command);

    AuthMemberInfo authenticateMember(Long adminId, Long memberId, ApartInfo apartInfo);

    void changeStatus(Long qnaId, ProcessingStatus processingStatus);

    Long writeAnswer(Long adminId, Long qnaId, String apartCode, WriteQnaAnswerCommand command);
}
