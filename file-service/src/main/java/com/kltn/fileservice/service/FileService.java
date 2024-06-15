package com.kltn.fileservice.service;

import com.kltn.fileservice.dto.ObjectFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<ObjectFileDTO> uploadFiles(String tenant, String channel, MultipartFile[] files);
    byte[] getFile(String tenant, String filePath);
    List<List<Object>> convertFileToJson(MultipartFile file) throws IOException;
}
