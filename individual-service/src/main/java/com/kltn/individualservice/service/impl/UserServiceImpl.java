package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.MinioChanel;
import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.dto.request.UploadRequest;
import com.kltn.individualservice.dto.request.UserRequestCRU;
import com.kltn.individualservice.dto.response.ObjectFileDTO;
import com.kltn.individualservice.entity.Document;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.exception.RequireException;
import com.kltn.individualservice.feign.FileServiceClient;
import com.kltn.individualservice.repository.UserRepository;
import com.kltn.individualservice.service.DocumentService;
import com.kltn.individualservice.service.RoleService;
import com.kltn.individualservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${minio.bucket}")
    private String bucket;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final FileServiceClient fileServiceClient;
    private final HttpServletRequest request;
    private final DocumentService documentService;

    public User updateUser(UserRequestCRU userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new NotFoundException(I18n.getMessage("msg.user.id")));

        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());

        if (userDto.getAvatarUpload() != null) {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            List<ObjectFileDTO> uploadResult = fileServiceClient.uploadFiles(header, new UploadRequest(bucket, MinioChanel.AVATAR, new MultipartFile[]{userDto.getAvatarUpload()}));
            Document document = documentService.saveDocument(new Document(MinioChanel.AVATAR, user.getId(), uploadResult.getFirst()));
            user.setAvatar(document.getId());
        } else {
            user.setAvatar(userDto.getAvatarId());
        }
        return userRepository.save(user);
    }

    public Integer findMaxNumberInCode(UserType type) {
        return userRepository.findMaxNumberInCode(findCodeByUserType(type));
    }

    private void validateUserImport(List<Object> rowData) {
        if (rowData.get(1) == null || rowData.get(1).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.firstname")));
        }
        if (rowData.get(2) == null || rowData.get(2).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.lastname")));
        }
        if (rowData.get(3) == null || rowData.get(3).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.phone")));
        }
        if (rowData.get(4) == null || rowData.get(4).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", "Email"));
        }
        if (rowData.get(5) == null || rowData.get(5).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.gender")));
        }
        if (rowData.get(6) == null || rowData.get(6).toString().isEmpty()) {
            throw new RequireException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.dateOfBirth")));
        }
    }

    private String findCodeByUserType(UserType type) {
        if (type == UserType.STUDENT) {
            return "A";
        } else if (type == UserType.TEACHER) {
            return "T";
        } else {
            return "E";
        }
    }
}
