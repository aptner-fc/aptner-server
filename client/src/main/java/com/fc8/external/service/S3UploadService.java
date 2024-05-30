package com.fc8.external.service;

import com.fc8.platform.common.properties.AptnerProperties;
import com.fc8.platform.common.s3.S3Uploader;
import com.fc8.platform.dto.record.UploadFileInfo;
import com.fc8.platform.dto.record.UploadImageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Uploader s3Uploader;

    public UploadImageInfo uploadPostImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(AptnerProperties.CATEGORY_POST, image);
    }

    public UploadFileInfo uploadPostFile(MultipartFile file) {
        return s3Uploader.uploadFileToBucket(AptnerProperties.CATEGORY_POST, file);
    }

}
