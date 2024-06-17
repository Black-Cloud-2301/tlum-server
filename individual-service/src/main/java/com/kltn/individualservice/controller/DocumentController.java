package com.kltn.individualservice.controller;

import com.kltn.individualservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/document")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping("/preview/{documentId}")
    public ResponseEntity<Resource> preview(@PathVariable("documentId") Long id) {
        return documentService.getPreviewResource(id);
    }
}
