package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentAdmin;
import com.fc8.facade.AdminFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.record.CurrentAdmin;
import com.fc8.platform.dto.request.SignInAdminRequest;
import com.fc8.platform.dto.request.SignUpAdminRequest;
import com.fc8.platform.dto.response.AuthMemberResponse;
import com.fc8.platform.dto.response.SignInAdminResponse;
import com.fc8.platform.dto.response.SignUpAdminResponse;
import com.fc8.platform.mapper.AdminMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/v1/api/admin", "/api/admin"})
@RequiredArgsConstructor
public class AdminController {

    private final AdminMapper adminMapper;
    private final AdminFacade adminFacade;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<CommonResponse<SignUpAdminResponse>> signUp(@Valid @RequestBody SignUpAdminRequest request) {
        var command = adminMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, adminFacade.signUp(command));
    }

    @PostMapping(value = "/sign-in")
    public ResponseEntity<CommonResponse<SignInAdminResponse>> signIn(@Valid @RequestBody SignInAdminRequest request) {
        var command = adminMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, adminFacade.signIn(command));
    }

    @CheckApartType
    @PostMapping(value = "/{apartCode}/auth/{memberId}")
    public ResponseEntity<CommonResponse<AuthMemberResponse>> authenticateMember(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentAdmin CurrentAdmin currentAdmin,
            @NotNull @PathVariable Long memberId) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, adminFacade.authenticateMember(currentAdmin.id(), memberId, apartCode));
    }

}
