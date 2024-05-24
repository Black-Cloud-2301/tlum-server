package com.kltn.authservice.business.token;

import com.kltn.authservice.utils.exception.CustomException;

public class TokenRefreshException extends CustomException {
    public TokenRefreshException(String error) {
        super(error);
    }
}