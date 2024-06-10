package com.kltn.individualservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "file-service")
public interface FileServiceClient {
    @PostMapping("/convert/file-to-json")
    List<List<Object>> convertFileToJson(@RequestPart MultipartFile file);
}
