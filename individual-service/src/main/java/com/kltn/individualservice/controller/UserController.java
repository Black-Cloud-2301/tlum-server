package com.kltn.individualservice.controller;

import com.kltn.individualservice.constant.UserType;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.service.UserService;
import com.kltn.individualservice.util.dto.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/import-users")
    ResponseEntity<Object> importUsers(@RequestPart("file") MultipartFile file, @RequestParam("type") UserType type) {
        List<User> users = userService.importUsers(file, type);
        return ResponseUtils.getResponseEntity(users);
    }
}
