package com.kltn.fileservice.controller;

import com.kltn.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/convert")
@RequiredArgsConstructor
public class ConvertFileController {
    private final FileService fileService;

    @PostMapping(value = "/file-to-json")
    List<List<Object>> convertFileToJson(@RequestPart MultipartFile file) throws IOException {
        return fileService.convertFileToJson(file);
    }
}
