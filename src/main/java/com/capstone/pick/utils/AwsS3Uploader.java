package com.capstone.pick.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String S3_USER_DIRECTORY_NAME = "user";
    private final AmazonS3Client amazonS3Client;

    public String uploadImage(String userId, MultipartFile multipartFile) {
        // 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        // 실제 S3 bucket 디렉토리명 설정
        String originalFileName = multipartFile.getOriginalFilename();
        int index = originalFileName.lastIndexOf(".");
        String ext = originalFileName.substring(index+1);

        String fileName = S3_USER_DIRECTORY_NAME + "/" + userId + "." + ext;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error("S3 파일 업로드에 실패하였습니다. {}", e.getMessage());
            throw new IllegalStateException("S3 파일 업로드 실패");
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public void deleteImage(String imageUrl) {
        String fileKey = S3_USER_DIRECTORY_NAME + "/" + imageUrl.split("/")[4];
        if(amazonS3Client.doesObjectExist(bucket, fileKey)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileKey));
        }
    }
}
