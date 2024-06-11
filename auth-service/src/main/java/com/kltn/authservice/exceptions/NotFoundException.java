package com.kltn.authservice.exceptions;

import com.kltn.authservice.config.I18n;
import com.kltn.authservice.utils.exception.CustomException;

public class NotFoundException extends CustomException {
    public NotFoundException(String mess) {
        super(I18n.getMessage("msg.error.not_found", mess));
    }
}
