package com.kltn.individualservice.util.exception.converter;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Component
public class DateConverter {

    public static LocalDate convertImport(String source) {
        DateTimeFormatter[] formatters = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH)
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(source, formatter);
            } catch (DateTimeParseException e) {
                // Continue to the next formatter
            }
        }

        throw new DateTimeParseException("Unable to parse date: " + source, source, 0);
    }
}