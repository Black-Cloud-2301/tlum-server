package com.kltn.individualservice.controller;

import com.kltn.individualservice.dto.request.UserRequestCRU;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.service.UserService;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping()
    ResponseEntity<Object> updateUser(@RequestBody UserRequestCRU userDto) {
        User updatedUser = userService.updateUser(userDto);
        return ResponseUtils.getResponseEntity(updatedUser);
    }
}
