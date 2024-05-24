package com.fc8.resolver;

import com.fc8.annotation.CheckCurrentMember;
import com.fc8.infrastructure.jwt.JwtTokenProvider;
import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.utils.ListUtils;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.dto.record.ApartInfo;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.repository.ApartRepository;
import com.fc8.platform.repository.MemberRepository;
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
public class CurrentMemberResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final ApartRepository apartRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CurrentMember.class) && parameter.hasParameterAnnotation(CheckCurrentMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = jwtTokenProvider.resolveToken(webRequest.getHeader(HttpHeaders.AUTHORIZATION));
        String email = jwtTokenProvider.getEmailByToken(accessToken);

        /**
         * TODO refactor : 조회 로직 변경
         */
        var member = memberRepository.getByEmail(email);
        var mainApart = apartRepository.getMainApartByMember(member);
        var apartList = apartRepository.getNotMainApartListByMember(member);
        validateCurrentMember(member);

        return CurrentMember.fromEntityWithApartInfo(member, ApartInfo.fromEntity(mainApart), ApartInfo.fromEntityList(apartList));
    }

    private void validateCurrentMember(Member member) {
        validateMember(member);
        validateApart(member);
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER);
        }
    }

    private void validateApart(Member member) {
        if (ListUtils.isEmpty(member.getApartMemberMappings())) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER_APART);
        }
    }

}
