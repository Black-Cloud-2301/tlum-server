package com.kltn.authservice.business.user;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User findById(Long userId);

    User findByCodeIgnoreCase(String code);

    User createUser(User user);

    List<User> importUsers(MultipartFile file, UserType type);
}
