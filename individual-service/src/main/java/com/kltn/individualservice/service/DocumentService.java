package com.kltn.individualservice.service;

import com.kltn.individualservice.entity.Document;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface DocumentService {
    Document saveDocument(Document document);
    ResponseEntity<Resource> getPreviewResource(Long id);
}
