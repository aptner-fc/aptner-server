package com.fc8.platform.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Server
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사 실패"),
    CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "제약 조건 위반"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생하였습니다."),
    S3_UPLOADER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 중 오류가 발생하였습니다."),

    /**
     * Common
     */
    COMMON_INVALID_PARAM(HttpStatus.BAD_REQUEST, "요청한 값이 올바르지 않습니다."),
    INVALID_AUTHENTICATION(HttpStatus.BAD_REQUEST, "인증이 올바르지 않습니다."),

    /**
     * Json Web Token
     */
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    MALFORMED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 형식의 토큰입니다."),
    SIGNATURE_EXCEPTION(HttpStatus.BAD_REQUEST, "토큰 서명이 올바르지 않습니다."),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 인자가 전달되었습니다."),

    /**
     * Authentication
     */
    INTERNAL_AUTHENTICATION_SERVICE(HttpStatus.BAD_REQUEST, "인증 서비스가 존재하지 않습니다."),
    NON_EXPIRED_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자 계정이 탈퇴되었습니다."),
    NON_LOCKED_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자 계정이 정지되었습니다."),
    DISABLE_ACCOUNT(HttpStatus.BAD_REQUEST, "사용자 계정은 비활성화 상태입니다."),
    EXPIRED_CREDENTIAL(HttpStatus.BAD_REQUEST, "사용자 인증 정보가 만료되었습니다."),

    /**
     * SMS
     */
    INVALID_OR_EXPIRED_SMS(HttpStatus.BAD_REQUEST, "SMS 인증 시간이 만료되었거나 인증을 받지 않았습니다."),
    INVALID_SMS_CODE(HttpStatus.BAD_REQUEST, "잘못된 SMS 코드입니다."),
    MAX_VERIFY_ATTEMPTS(HttpStatus.BAD_REQUEST, "SMS 인증 시도에 실패했습니다."),

    /**
     * Admin, Member
     */
    NOT_FOUND_ADMIN(HttpStatus.BAD_REQUEST, "어드민 정보를 찾을 수 없습니다."),
    NOT_FOUND_ADMIN_APART(HttpStatus.BAD_REQUEST, "어드민 관리 아파트를 찾을 수 없습니다."),

    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),
    NOT_FOUND_MEMBER_APART(HttpStatus.BAD_REQUEST, "회원 메인 아파트를 찾을 수 없습니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),
    FAIL_LOGIN(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 잘못되었습니다."),

    /**
     * TERMS
     */
    NOT_FOUND_TERMS(HttpStatus.BAD_REQUEST, "약관 정보를 찾을 수 없습니다."),
    MISMATCH_TERMS_AGREEMENT(HttpStatus.BAD_REQUEST, "사용중인 약관과 요청한 약관 동의 리스트가 다릅니다."),
    MISSING_REQUIRED_AGREEMENT(HttpStatus.BAD_REQUEST, "필수 약관에 동의하지 않았습니다."),

    /**
     * Apart
     */
    NOT_PERMISSION_APART(HttpStatus.BAD_REQUEST, "해당 아파트의 권한이 없습니다."),
    NOT_FOUND_USED_APART(HttpStatus.BAD_REQUEST, "사용중인 아파트 정보를 찾을 수 없습니다."),

    /**
     * Category
     */
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."),

    /**
     * POST & QNA
     */
    NOT_FOUND_POST(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    NOT_FOUND_POST_COMMENT(HttpStatus.BAD_REQUEST, "게시글 댓글을 찾을 수 없습니다."),
    NOT_POST_WRITER(HttpStatus.BAD_REQUEST, "게시글 작성자가 아닙니다."),
    NOT_POST_COMMENT_WRITER(HttpStatus.BAD_REQUEST, "댓글 작성자가 아닙니다."),
    ALREADY_DELETED_POST(HttpStatus.BAD_REQUEST, "이미 삭제된 게시글입니다."),
    ALREADY_DELETED_POST_COMMENT(HttpStatus.BAD_REQUEST, "이미 삭제된 댓글입니다."),
    ALREADY_DELETED_POST_COMMENT_IMAGE(HttpStatus.BAD_REQUEST, "이미 삭제된 댓글 이미지입니다."),

    /**
     * EMOJI
     */
    ALREADY_REGISTER_EMOJI(HttpStatus.BAD_REQUEST, "이미 해당 감정 표현이 등록되어있습니다."),
    NOT_FOUND_EMOJI(HttpStatus.BAD_REQUEST, "회원이 등록한 감정 표현을 찾을 수 없습니다."),

    /**
     * FILE
     */
    EXCEEDED_FILE_SIZE(HttpStatus.BAD_REQUEST, "파일의 허용 용량을 초과했습니다."),
    EXCEEDED_FILE_COUNT(HttpStatus.BAD_REQUEST, "파일 첨부 허용 개수를 초과했습니다."),

    /**
     * SEARCH
     */
    NOT_FOUND_SEARCH_TYPE(HttpStatus.BAD_REQUEST, "검색 타입을 찾을 수 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
