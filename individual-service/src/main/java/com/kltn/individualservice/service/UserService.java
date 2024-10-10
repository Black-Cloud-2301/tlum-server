package com.kltn.individualservice.service;

import com.kltn.sharedto.constants.NotificationObject;
import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.dto.request.UserRequestCRU;
import com.kltn.individualservice.entity.User;

import java.util.List;

public interface UserService {
    User updateUser(UserRequestCRU userDto);
    Integer findMaxNumberInCode(UserType type);
    List<Long> getUserIdsByObject(NotificationObject object);
}
