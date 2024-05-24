package com.kltn.authservice.business.user.exceptions;

import com.kltn.authservice.utils.exception.CustomException;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super("USER_NOT_FOUND");
    }
}
