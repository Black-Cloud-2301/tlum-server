package com.kltn.individualservice.constant;

import com.kltn.individualservice.util.exception.CustomException;

public enum Gender {
    MALE, FEMALE;

    public static Gender fromString(String value) {
        if ("Nam".equalsIgnoreCase(value)) {
            return MALE;
        } else if ("Nữ".equalsIgnoreCase(value)) {
            return FEMALE;
        } else {
            throw new CustomException("Invalid gender value: " + value);
        }
    }
}