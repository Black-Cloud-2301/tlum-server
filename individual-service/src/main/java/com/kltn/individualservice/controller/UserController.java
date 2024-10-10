package com.kltn.individualservice.controller;

import com.kltn.sharedto.constants.NotificationObject;
import com.kltn.individualservice.dto.request.UserRequestCRU;
import com.kltn.individualservice.entity.User;
import com.kltn.individualservice.service.UserService;
import com.kltn.individualservice.util.dto.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping
    ResponseEntity<Object> updateUser(@ModelAttribute UserRequestCRU userDto) {
        User updatedUser = userService.updateUser(userDto);
        return ResponseUtils.getResponseEntity(updatedUser);
    }

    @GetMapping("/by-object")
    ResponseEntity<Object> getUserIdsByObject(@RequestParam NotificationObject object) {
        return ResponseUtils.getResponseEntity(userService.getUserIdsByObject(object));
    }
}
