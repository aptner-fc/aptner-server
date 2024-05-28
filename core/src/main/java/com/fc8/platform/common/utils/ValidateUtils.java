package com.fc8.platform.common.utils;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.enums.CategoryType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidateUtils {

    public static void validatePassword(String password, String encPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password, encPassword)) {
            throw new InvalidParamException(ErrorCode.FAIL_LOGIN);
        }
    }

    public static void validateChildCategoryType(CategoryType type, Category category) {
        isChildCategory(category);

        Optional.ofNullable(type)
                .filter(t -> t.equals(category.getType()))
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_CATEGORY));
    }

    private static void isChildCategory(Category category) {
        if (category.getParent() == null) {
            throw new InvalidParamException(ErrorCode.NOT_FOUND_CATEGORY);
        }
    }

}
