package com.kltn.configservice.business._init.data;

import com.kltn.configservice.business._init.DataImporter;
import com.kltn.configservice.business.file.File;
import com.kltn.configservice.business.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinioImporter extends DataImporter {
    private final FileRepository minioRepository;

    @Value("${config.current-profile}")
    private String profile;

    @Value("${config.minio.baseUrl}")
    private String baseUrl;

    @Value("${config.minio.accessKey}")
    private String accessKey;

    @Value("${config.minio.secretKey}")
    private String secretKey;

    @Value("${config.minio.bucket}")
    private String bucket;

    @Override
    public void importData() {
        if (!minioRepository.existsByProfile(profile)) {
            File newMinio = new File(null, "", profile, baseUrl, accessKey, secretKey, bucket);
            minioRepository.save(newMinio);
        }
    }
}
