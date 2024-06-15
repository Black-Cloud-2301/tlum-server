package com.kltn.configservice.business.file;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("file-service")
public class File {
    @Id
    String id;
    String label;
    String profile;
    Source source;

    public File(String id, String label, String profile, String baseUrl, String accessKey, String secretKey, String bucket) {
        this.id = id;
        this.label = label;
        this.profile = profile;
        this.source = new Source(baseUrl, accessKey, secretKey, bucket);
    }

    static class Source {
        Minio minio;

        Source(String baseUrl, String accessKey, String secretKey, String bucket) {
            this.minio = new Minio(baseUrl, accessKey, secretKey, bucket);
        }
    }

    static class Minio {
        String baseUrl;
        String accessKey;
        String secretKey;
        String bucket;

        Minio(String baseUrl, String accessKey, String secretKey, String bucket) {
            this.baseUrl = baseUrl;
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.bucket = bucket;
        }
    }
}
