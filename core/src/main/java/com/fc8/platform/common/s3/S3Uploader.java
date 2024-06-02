package com.fc8.platform.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.properties.AptnerProperties;
import com.fc8.platform.dto.record.UploadFileInfo;
import com.fc8.platform.dto.record.UploadImageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public UploadImageInfo uploadMultipartFileToBucket(String category, MultipartFile file) {
        String filePath = getFilePath(category, file.getName());
        ObjectMetadata metadata = createMetadataFromFile(file);

        try {
            amazonS3.putObject(
                    new PutObjectRequest(bucket, filePath, file.getInputStream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );
        } catch (Exception e) {
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }

        return new UploadImageInfo(getUrlFromBucket(filePath));
    }

    public UploadFileInfo uploadFileToBucket(String category, MultipartFile file) {
        String filePath = getFilePath(category, file.getName());
        ObjectMetadata metadata = createMetadataFromFile(file);
        UploadFileInfo uploadFileInfo;

        try {
            String originalFilename = file.getOriginalFilename();
            long fileSize = file.getSize();
            String fileExtension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(AptnerProperties.FILE_DOT) + 1);
            amazonS3.putObject(
                    new PutObjectRequest(bucket, filePath, file.getInputStream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            uploadFileInfo = new UploadFileInfo(fileSize, file.getName(), fileExtension, originalFilename.replace(AptnerProperties.FILE_DOT.concat(fileExtension), ""), getUrlFromBucket(filePath));
        } catch (Exception e) {
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }

        return uploadFileInfo;
    }

    private String getFilePath(String category, String fileName) {
        return category + File.separator + createDatePath() + File.separator + generateRandomFilePrefix() + fileName;
    }

    private String createDatePath() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(AptnerProperties.DATE_FORMAT_YYYYMMDD);

        return now.format(dateTimeFormatter);
    }

    private ObjectMetadata createMetadataFromFile(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    private ObjectMetadata createMetadataFromFile(File file) {
        ObjectMetadata metadata = new ObjectMetadata();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            metadata.setContentLength(file.length());
            metadata.setContentType(URLConnection.guessContentTypeFromStream(fileInputStream));
        } catch (IOException e) {
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }

        return metadata;
    }

    private String generateRandomFilePrefix() {
        String randomUUID = UUID.randomUUID().toString();
        String cleanedUUID = randomUUID.replace("-", "");
        return cleanedUUID.substring(0, 16);
    }

    private String getUrlFromBucket(String fileKey) {
        return amazonS3.getUrl(bucket, fileKey).toString();
    }

}
