package com.kltn.authservice.exceptions.user;

import com.kltn.authservice.config.I18n;
import com.kltn.authservice.utils.exception.CustomException;

public class IncorrectPasswordException extends CustomException {
    public IncorrectPasswordException() {
        super(I18n.getMessage("msg.status.unauthorized"));
    }
}
