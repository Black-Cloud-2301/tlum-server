package com.kltn.individualservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ObjectFileDTO {
    String linkUrl;
    String linkUrlPublic;
    String filePath;
    String fileName;
    Long fileSize;
}