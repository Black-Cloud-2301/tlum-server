package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.constant.MinioChanel;
import com.kltn.individualservice.entity.Document;
import com.kltn.individualservice.feign.FileServiceClient;
import com.kltn.individualservice.repository.DocumentRepository;
import com.kltn.individualservice.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final FileServiceClient fileServiceClient;
    private final HttpServletRequest request;

    @Value("${minio.bucket}")
    private String tenant;

    @Override
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public ResponseEntity<Resource> getPreviewResource(Long id) {
        Optional<Document> document = documentRepository.findById(id);
        if (document.isPresent()) {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            byte[] fileBytes = fileServiceClient.getFile(header, tenant, document.get().getFilePath());
            String extension = getExtension(document.get().getFileName());
            List<String> fileTypes = Arrays.asList("pdf", "jpeg", "jpg", "png");
            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = MediaTypeFactory.getMediaType(document.get().getFileName()).orElse(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentType(mediaType);
            if (fileTypes.contains(extension)) {
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + document.get().getFileName());
                headers.set("X-Frame-Options", "SAMEORIGIN");
            } else {
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.get().getFileName());
            }
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ByteArrayResource(fileBytes));

        }
        throw new RuntimeException("File not found!");
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
