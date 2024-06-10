package com.kltn.fileservice.service;

import com.kltn.fileservice.constants.UserType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConvertFileService {
    List<List<Object>> convertFileToJson(MultipartFile file, UserType type) throws IOException;
}
