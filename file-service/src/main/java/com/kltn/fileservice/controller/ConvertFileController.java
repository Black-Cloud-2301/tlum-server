package com.kltn.fileservice.controller;

import com.kltn.fileservice.service.ConvertFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/convert")
public class ConvertFileController {
    private final ConvertFileService convertFileService;

    public ConvertFileController(ConvertFileService convertFileService) {
        this.convertFileService = convertFileService;
    }

    @PostMapping(value = "/file-to-json")
    List<List<Object>> convertFileToJson(@RequestPart MultipartFile file) throws IOException {
        return convertFileService.convertFileToJson(file);
    }
}
