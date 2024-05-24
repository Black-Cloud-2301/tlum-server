package com.kltn.authservice.business.user.exceptions;

import com.kltn.authservice.utils.exception.CustomException;

public class IncorrectPasswordException extends CustomException {
    public IncorrectPasswordException() {
        super("INCORRECT_PASSWORD");
    }
}
