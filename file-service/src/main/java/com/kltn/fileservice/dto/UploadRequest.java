package com.kltn.fileservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UploadRequest {
    private String tenant;
    private String channel;
    private MultipartFile[] files;
}
