package com.kltn.authservice.business.user.exceptions;

import com.kltn.authservice.config.I18n;
import com.kltn.authservice.utils.exception.CustomException;

public class UserInactiveException extends CustomException {
    public UserInactiveException() {
        super(I18n.getMessage("msg.user.inactivate"));
    }
}
