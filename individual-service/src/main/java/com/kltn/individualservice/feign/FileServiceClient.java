package com.kltn.individualservice.feign;

import com.kltn.individualservice.dto.request.UploadRequest;
import com.kltn.individualservice.dto.response.ObjectFileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "file-service")
public interface FileServiceClient {
    @PostMapping(path = "/v1/convert/file-to-json",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<List<Object>> convertFileToJson(@RequestHeader("Authorization") String token, @RequestPart("file") MultipartFile file);

    @PostMapping(path = "/v1/file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<ObjectFileDTO> uploadFiles(@RequestHeader("Authorization") String token, @ModelAttribute UploadRequest request);

    @GetMapping(path = "/v1/file")
    byte[] getFile(@RequestHeader("Authorization") String token, @RequestParam String tenant, @RequestParam String filePath);
}
