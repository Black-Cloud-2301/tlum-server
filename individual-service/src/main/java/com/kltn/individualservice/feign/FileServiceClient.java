package com.kltn.individualservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "file-service")
public interface FileServiceClient {
    @PostMapping(path = "/convert/file-to-json",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<List<Object>> convertFileToJson(@RequestHeader("Authorization") String token, @RequestPart("file") MultipartFile file);
}
