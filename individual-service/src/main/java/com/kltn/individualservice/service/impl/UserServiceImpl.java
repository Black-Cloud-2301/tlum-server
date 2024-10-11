package com.kltn.individualservice.service.impl;

import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.*;
import com.kltn.individualservice.dto.request.UploadRequest;
import com.kltn.individualservice.dto.request.UserRequestCRU;
import com.kltn.individualservice.dto.response.ObjectFileDTO;
import com.kltn.individualservice.entity.Document;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.exception.NotFoundException;
import com.kltn.individualservice.feign.FileServiceClient;
import com.kltn.individualservice.repository.UserRepository;
import com.kltn.individualservice.service.DocumentService;
import com.kltn.individualservice.service.UserService;
import com.kltn.individualservice.constant.NotificationObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${minio.bucket}")
    private String bucket;

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

    @Override
    public List<Long> getUserIdsByObject(NotificationObject object) {
        List<Long> userIds = new ArrayList<>();
        if (object == NotificationObject.ALL) {
            userIds.addAll(userRepository.findStudentIdsByObject(List.of(StudentStatus.REGISTERED, StudentStatus.STUDYING)));
            userIds.addAll(userRepository.findTeacherIdsByObject(List.of(EmployeeStatus.OFFICIAL, EmployeeStatus.PROBATION)));
        } else if (object == NotificationObject.STUDENT) {
            userIds.addAll(userRepository.findStudentIdsByObject(List.of(StudentStatus.REGISTERED, StudentStatus.STUDYING)));
        } else if (object == NotificationObject.TEACHER) {
            userIds.addAll(userRepository.findTeacherIdsByObject(List.of(EmployeeStatus.OFFICIAL, EmployeeStatus.PROBATION)));
//        } else if (object == NotificationObject.EMPLOYEE) {
//            userIds.addAll(userRepository.findEmployeeIdsByObject(List.of(EmployeeStatus.OFFICIAL, EmployeeStatus.PROBATION)));
        }
        return userIds;
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
