package com.kltn.individualservice.service;

import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.dto.request.UserRequestCRU;
import com.kltn.individualservice.entity.User;

public interface UserService {
    User updateUser(UserRequestCRU userDto);
    Integer findMaxNumberInCode(UserType type);
}
