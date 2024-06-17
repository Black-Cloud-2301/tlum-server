package com.kltn.fileservice.controller;

import com.kltn.fileservice.dto.ObjectFileDTO;
import com.kltn.fileservice.dto.UploadRequest;
import com.kltn.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping
    public List<ObjectFileDTO> uploadFile(@ModelAttribute UploadRequest request) {
        return fileService.uploadFiles(request.getTenant(), request.getChannel(), request.getFiles());
    }

    @GetMapping
    public byte[] getFile(@RequestParam String tenant, @RequestParam String filePath) {
        return fileService.getFile(tenant, filePath);
    }
}
