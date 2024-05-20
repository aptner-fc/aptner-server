package com.fc8.service;


import com.fc8.platform.dto.command.SignInMemberCommand;
import com.fc8.platform.dto.command.SignUpMemberCommand;
import com.fc8.platform.dto.record.SignInMemberInfo;

public interface MemberService {

    Long signUp(SignUpMemberCommand command);

    SignInMemberInfo signIn(SignInMemberCommand command);

}
