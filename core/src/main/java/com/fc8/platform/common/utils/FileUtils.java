package com.fc8.platform.common.utils;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.properties.AptnerProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static void validateFile(MultipartFile file) {
        // 1. 최대 크기 검사
        validateFileMaxSize(file, AptnerProperties.FILE_MAX_SIZE_MB);

        // 2. 파일 확장자 검사 TODO
    }

    public static void validateFiles(List<MultipartFile> files) {
        // 1. 최대 크기 검사
        if (files == null || files.isEmpty()) {
            throw new InvalidParamException(ErrorCode.COMMON_INVALID_PARAM);
        }

        files.forEach(file -> {
            // 1. 최대 크기 검사
            validateFileMaxSize(file, AptnerProperties.FILE_MAX_SIZE_MB);

            // 2. 파일 확장자 검사 TODO
        });
    }

    public static void validateFileMaxSize(MultipartFile image, int megabyteSize) {
        Optional.ofNullable(image)
                .filter(img -> {
                    if (megabyteSize <= 0) {
                        throw new InvalidParamException(ErrorCode.COMMON_INVALID_PARAM);
                    }
                    return !img.isEmpty();
                })
                .ifPresent(img -> {
                    int maxMegabyte = megabyteSize * 1024 * 1024;
                    if (img.getSize() > maxMegabyte) {
                        throw new InvalidParamException(ErrorCode.EXCEEDED_FILE_SIZE);
                    }
                });
    }
}
