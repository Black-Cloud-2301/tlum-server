package com.kltn.authservice.business.user.exceptions;

import com.kltn.authservice.utils.exception.CustomException;

public class UserInactiveException extends CustomException {
    public UserInactiveException() {
        super("USER_INACTIVE");
    }
}
