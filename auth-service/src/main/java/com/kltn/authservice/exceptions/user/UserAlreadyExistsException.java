package com.kltn.authservice.exceptions.user;

import com.kltn.authservice.config.I18n;
import com.kltn.authservice.utils.exception.CustomException;

public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException() {
        super(I18n.getMessage("msg.user.exists"));
    }
}