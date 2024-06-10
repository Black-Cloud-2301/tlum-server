package com.kltn.individualservice.util.exception.converter;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DateConverter {

    public static LocalDate convertImport(String source) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        return LocalDate.parse(source, inputFormatter);
    }
}