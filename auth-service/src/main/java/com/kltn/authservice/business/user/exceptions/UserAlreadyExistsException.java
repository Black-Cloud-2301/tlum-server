package com.kltn.authservice.business.user.exceptions;

import com.kltn.authservice.utils.exception.CustomException;

public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException() {
        super("UserAlreadyExists");
    }
}
