package com.kltn.individualservice.service;

import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<User> importUsers(MultipartFile file, UserType type);
}
