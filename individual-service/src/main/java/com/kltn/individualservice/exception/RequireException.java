package com.kltn.individualservice.exception;


import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.util.exception.CustomException;

public class RequireException extends CustomException {
    public RequireException(String mess) {
        super(I18n.getMessage("msg.validate.require", mess));
    }
}
