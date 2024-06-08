package com.fc8.platform.common.utils;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.properties.AptnerProperties;
import com.fc8.platform.domain.enums.FileType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    private static final String[] FILE_EXTENSIONS = {
            "HWP", "DOC", "DOCX", "XLS", "PPT", "PPTX", "PDF", "JPG", "JPEG",
            "PNG", "GIF", "BMP", "MOV", "AVI", "MPG", "3GP", "3G2", "MIDI",
            "MID", "MP3", "MP4", "WebM", "WMV"
    };

    private static final String[] THUMBNAIL_EXTENSIONS = {
            "JPG", "JPEG", "PNG", "GIF"
    };

    public static void validateFile(MultipartFile file) {
        // 1. 최대 크기 검사
        validateFileMaxSize(file, AptnerProperties.FILE_MAX_SIZE_MB);

        // 2. 파일 확장자 검사
        validateExtension(FileType.FILE, file);
    }

    public static void validateFiles(List<MultipartFile> files) {
        // 1. 최대 크기 검사
        if (files == null || files.isEmpty()) {
            throw new InvalidParamException(ErrorCode.COMMON_INVALID_PARAM);
        }

        files.forEach(file -> {
            // 1. 최대 크기 검사
            validateFileMaxSize(file, AptnerProperties.FILE_MAX_SIZE_MB);
            // 2. 확장자 검사
            validateExtension(FileType.FILE, file);
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

    private static void validateExtension(FileType fileType, MultipartFile file) {
        if (file == null) {
            throw new InvalidParamException(ErrorCode.COMMON_INVALID_PARAM);
        }

        String[] validExtensions = Optional.ofNullable(fileType)
                .map(ft -> {
                    if (ft == FileType.FILE) {
                        return FILE_EXTENSIONS;
                    } else if (ft == FileType.THUMBNAIL) {
                        return THUMBNAIL_EXTENSIONS;
                    } else {
                        throw new InvalidParamException(ErrorCode.COMMON_INVALID_PARAM);
                    }
                })
                .orElseThrow(() -> new InvalidParamException(ErrorCode.COMMON_INVALID_PARAM));

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtensionByOriginalFilename(originalFilename);

        if (!Arrays.asList(validExtensions).contains(fileExtension.toUpperCase())) {
            throw new InvalidParamException(ErrorCode.INVALID_EXTENSIONS);
        }
    }

    public static String getFileExtensionByOriginalFilename(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(AptnerProperties.FILE_DOT)) {
            throw new InvalidParamException(ErrorCode.INVALID_FILE_NAME_OR_EXTENSIONS);
        }

        return Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(AptnerProperties.FILE_DOT) + 1);
    }
}
