package com.kltn.fileservice.controller;

import com.kltn.fileservice.constants.UserType;
import com.kltn.fileservice.service.ConvertFileService;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/file-to-json")
    List<List<Object>> convertFileToJson(@RequestPart MultipartFile file, @RequestParam UserType type) throws IOException {
        return convertFileService.convertFileToJson(file, type);
    }
}
