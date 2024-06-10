package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.UserType;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ImportUsersRequest {
    MultipartFile file;
    UserType type;
}
