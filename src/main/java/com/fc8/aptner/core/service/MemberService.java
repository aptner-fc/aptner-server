package com.fc8.aptner.core.service;

import com.fc8.aptner.core.dto.SignInMemberInfo;
import com.fc8.aptner.core.dto.command.SignInMemberCommand;
import com.fc8.aptner.core.dto.command.SignUpMemberCommand;

public interface MemberService {

    Long signUp(SignUpMemberCommand command);

    SignInMemberInfo signIn(SignInMemberCommand command);
}
