package com.kltn.individualservice.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class I18n {
    final static Logger LOGGER = LoggerFactory.getLogger(I18n.class);
    static MessageSource messageSource;


    @Autowired
    I18n(ResourceBundleMessageSource messageSource) {
        I18n.messageSource = messageSource;
    }

    public static String getMessage(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage() == null || locale.getLanguage().isEmpty()) {
            locale = Locale.of("vi");
        }
        try {
            return messageSource.getMessage(msgCode, null, locale);
        } catch (Exception e) {
            LOGGER.error("Error getMessage:", e);
            return msgCode;
        }
    }

    public static String getMessage(String msgCode, Object... arg) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage() == null || locale.getLanguage().isEmpty()) {
            locale = Locale.of("vi");
        }
        return String.format(messageSource.getMessage(msgCode, null, locale), arg);
    }
}

