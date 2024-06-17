package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.Gender;
import com.kltn.individualservice.constant.MinioChanel;
import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.dto.request.UploadRequest;
import com.kltn.individualservice.dto.request.UserRequestCRU;
import com.kltn.individualservice.dto.response.ObjectFileDTO;
import com.kltn.individualservice.entity.Document;
import com.kltn.individualservice.entity.Role;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.exception.RequireException;
import com.kltn.individualservice.feign.FileServiceClient;
import com.kltn.individualservice.repository.UserRepository;
import com.kltn.individualservice.service.DocumentService;
import com.kltn.individualservice.service.RoleService;
import com.kltn.individualservice.service.UserService;
import com.kltn.individualservice.util.exception.converter.DateConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${individual.defaultPassword}")
    private String defaultPassword;

    @Value("${minio.bucket}")
    private String bucket;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final FileServiceClient fileServiceClient;
    private final HttpServletRequest request;
    private final DocumentService documentService;

    public List<User> importUsers(MultipartFile file, UserType type) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        List<List<Object>> excelData = fileServiceClient.convertFileToJson(header, file);
        List<User> users = new ArrayList<>();
        Role defaultRole = roleService.getRoleByCode(type.name());
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        AtomicInteger currentMaxCode = new AtomicInteger(userRepository.findMaxNumberInCode());

        for (List<Object> rowData : excelData) {
            this.validateUserImport(rowData);
            User user = new User();
            user.setCode(Optional.ofNullable((String) rowData.get(0)).orElseGet(() -> "A" + (currentMaxCode.incrementAndGet())));
            user.setFirstname((String) rowData.get(1));
            user.setLastname((String) rowData.get(2));
            user.setPhoneNumber((String) rowData.get(3));
            user.setEmail((String) rowData.get(4));
            user.setGender(Gender.fromString((String) rowData.get(5)));
            user.setDateOfBirth(DateConverter.convertImport((String) rowData.get(6)));
            user.setAddress((String) rowData.get(7));
            user.setPassword(passwordEncoder.encode(defaultPassword));
            user.setRoles(roles);
            users.add(user);
        }
        return userRepository.saveAll(users);
    }


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

    private String generateCode() {
        return "A" + (userRepository.findMaxNumberInCode() + 1);
    }
}
