package com.kltn.authservice.business.user.exceptions;

import com.kltn.authservice.config.I18n;
import com.kltn.authservice.utils.exception.CustomException;

public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException() {
        super(I18n.getMessage("msg.user.exists"));
    }
}
