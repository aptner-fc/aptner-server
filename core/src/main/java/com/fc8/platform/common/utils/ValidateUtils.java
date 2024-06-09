package com.fc8.platform.common.utils;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.properties.AptnerProperties;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.enums.CategoryType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidateUtils {

    public static void validatePassword(String password, String encPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password, encPassword)) {
            throw new InvalidParamException(ErrorCode.COMMON_INVALID_PARAM);
        }
    }

    public static void validateChangePassword(String currentPassword, String newPassword, String confirmNewPassword) {
        validateMatchNewPassword(currentPassword, newPassword);
        validateConfirmPassword(newPassword, confirmNewPassword);
    }

    public static void validateInteriorPost(Category category) {
        if (!Objects.equals(category.getCode(), AptnerProperties.CATEGORY_CODE_POST_INTERIOR)) {
            throw new InvalidParamException(ErrorCode.ONLY_CATEGORY_INTERIOR);
        }
    }

    private static void validateMatchNewPassword(String currentPassword, String newPassword) {
        if (Objects.equals(currentPassword, newPassword)) {
            throw new InvalidParamException(ErrorCode.MATCH_PASSWORD_AND_NEW_PASSWORD);
        }
    }

    public static void validateConfirmPassword(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new InvalidParamException(ErrorCode.NOT_MATCH_CONFIRM);
        }
    }

    public static void validateParentCategoryCode(String categoryCode, Category category) {
        isParentCategory(category);
        validateCategoryCode(categoryCode, category);
    }

    public static void validateChildCategoryType(CategoryType type, Category category) {
        isChildCategory(category);
        validateCategoryType(type, category);
    }

    private static void isChildCategory(Category category) {
        if (category.getParent() == null) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_CATEGORY);
        }
    }

    private static void isParentCategory(Category category) {
        if (category.getParent() != null) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_CATEGORY);
        }
    }

    private static void validateCategoryType(CategoryType type, Category category) {
        if (type == null || !type.equals(category.getType())) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_CATEGORY);
        }
    }

    private static void validateCategoryCode(String code, Category category) {
        if (!StringUtils.hasText(code) || !code.equals(category.getCode())) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_CATEGORY);
        }
    }

}
