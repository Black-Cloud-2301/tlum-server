package com.kltn.minioservice.service;

import com.tlum.minioservice.dto.ObjectFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<ObjectFileDTO> uploadFiles(String tenant, String channel, MultipartFile[] files);

    byte[] getFile(String tenant, String filePath);
}
