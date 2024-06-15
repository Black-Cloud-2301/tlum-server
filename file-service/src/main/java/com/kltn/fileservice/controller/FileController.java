package com.kltn.fileservice.controller;

import com.kltn.fileservice.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/convert")
public class FileController {
    private final FileService convertFileService;

    public FileController(FileService convertFileService) {
        this.convertFileService = convertFileService;
    }

    @PostMapping(value = "/file-to-json")
    List<List<Object>> convertFileToJson(@RequestPart MultipartFile file) throws IOException {
        return convertFileService.convertFileToJson(file);
    }
}
