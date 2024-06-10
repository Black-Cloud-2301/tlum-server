package com.kltn.authservice.business.user.exceptions;

import com.kltn.authservice.config.I18n;
import com.kltn.authservice.utils.exception.CustomException;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(I18n.getMessage("msg.user.not_found"));
    }
}
