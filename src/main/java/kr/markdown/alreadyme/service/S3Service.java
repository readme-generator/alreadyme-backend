package kr.markdown.alreadyme.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(File file, String dirName) {
        String filePath = dirName + "/" + file.getName();
        return putS3(file, filePath);
    }

    private String putS3(File file, String filePath) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, filePath, file)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3Client.getUrl(bucket, filePath).toString();
    }
}
