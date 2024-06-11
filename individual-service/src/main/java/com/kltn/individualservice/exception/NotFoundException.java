package com.kltn.individualservice.exception;


import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.util.exception.CustomException;

public class NotFoundException extends CustomException {
    public NotFoundException(String mess) {
        super(I18n.getMessage("msg.error.not_found", mess));
    }
}
