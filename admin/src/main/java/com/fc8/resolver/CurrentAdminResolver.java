package com.fc8.resolver;

import com.fc8.annotation.CheckCurrentAdmin;
import com.fc8.infrastructure.jwt.JwtTokenProvider;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.dto.record.ApartInfo;
import com.fc8.platform.dto.record.CurrentAdmin;
import com.fc8.platform.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CurrentAdminResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final AdminRepository adminRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CurrentAdmin.class) && parameter.hasParameterAnnotation(CheckCurrentAdmin.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = jwtTokenProvider.resolveToken(webRequest.getHeader(HttpHeaders.AUTHORIZATION));
        String email = jwtTokenProvider.getEmailByToken(accessToken);

        Admin admin = adminRepository.getAdminWithApartByEmail(email);
        validateCurrentAdmin(admin);

        return CurrentAdmin.fromEntityWithApartInfo(admin, ApartInfo.fromEntity(admin.getApart()));
    }

    private void validateCurrentAdmin(Admin admin) {
        validateAdmin(admin);
        validateApart(admin);
    }

    private void validateAdmin(Admin admin) {
        if (admin == null) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_ADMIN);
        }
    }

    private  void validateApart(Admin admin) {
        if (admin.getApart() == null) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_ADMIN_APART);
        }
    }

}
