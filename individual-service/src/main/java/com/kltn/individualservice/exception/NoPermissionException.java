package com.kltn.individualservice.exception;


import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.util.exception.CustomException;

public class NoPermissionException extends CustomException {
    public NoPermissionException() {
        super(I18n.getMessage("msg.no_permission"));
    }
}
