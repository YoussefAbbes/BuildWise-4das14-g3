package com.dynamika.buildwise.workforce.files;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Service
public class FileStorageService {
    private static final Duration URL_TTL = Duration.ofMinutes(5);

    private final S3Client s3;
    private final S3Presigner presigner;
    private final String bucket;
    private final String keyPrefix;

    public FileStorageService(S3Client s3, S3Presigner presigner,
                              @Value("${buildwise.s3.bucket}") String bucket,
                              @Value("${buildwise.s3.key-prefix:}") String keyPrefix) {
        this.s3 = s3;
        this.presigner = presigner;
        this.bucket = bucket;
        this.keyPrefix = keyPrefix;
    }

    public void upload(String key, InputStream data, long size, String contentType) {
        s3.putObject(r -> r.bucket(bucket).key(keyPrefix + key).contentType(contentType),
                RequestBody.fromInputStream(data, size));
    }

    public ResponseInputStream<GetObjectResponse> download(String key) {
        return s3.getObject(r -> r.bucket(bucket).key(keyPrefix + key));
    }

    public void delete(String key) {
        s3.deleteObject(r -> r.bucket(bucket).key(keyPrefix + key));
    }

    public List<String> list(String prefix) {
        return s3.listObjectsV2Paginator(r -> r.bucket(bucket).prefix(keyPrefix + prefix))
                .contents().stream()
                .map(o -> o.key().substring(keyPrefix.length()))
                .toList();
    }

    public String presignedGetUrl(String key) {
        return presigner.presignGetObject(p -> p.signatureDuration(URL_TTL)
                        .getObjectRequest(r -> r.bucket(bucket).key(keyPrefix + key)))
                .url().toString();
    }

    public String presignedPutUrl(String key, String contentType) {
        return presigner.presignPutObject(p -> p.signatureDuration(URL_TTL)
                        .putObjectRequest(r -> r.bucket(bucket).key(keyPrefix + key).contentType(contentType)))
                .url().toString();
    }
}
