package com.kltn.individualservice.util.exception;

import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.ErrorApp;
import com.kltn.individualservice.dto.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static ResponseEntity<Object> errorResponse(ErrorApp errorApp, String path, HttpStatus httpStatus) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(errorApp.getCode());
        baseResponse.setMessage(errorApp.getDescription());
        baseResponse.setPath(path);
        baseResponse.setTimestamp(new Date(System.currentTimeMillis()));
        baseResponse.setStatus(httpStatus.value());
        return new ResponseEntity<>(baseResponse, httpStatus);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidateException(ConstraintViolationException e, HttpServletRequest request) {
        log.error("Has ERROR", e);
        return HandleExceptionUtils.errorResponse(HandleExceptionUtils.getMsgValidateException(e), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException e, HttpServletRequest request) {
        Throwable originalException = e.getRootCause();
        if (originalException instanceof ConstraintViolationException) {
            return handleValidateException((ConstraintViolationException) originalException, request);
        }
        return handleException(e, request);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex, HttpServletRequest request) {
        if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
            log.error("Has ERROR CustomException with message = {}, at = {}", ex.getMessage(), ex.getStackTrace()[0].toString());
        } else {
            log.error("Has ERROR CustomException with code = {}, message = {}", ex.getMessage(), ex.getMessage());
        }
        if (Objects.isNull(ex.getErrorApp()) && Objects.nonNull(ex.getCodeError())) {
            return errorResponse(ex.getErrorApp(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
        }
        return HandleExceptionUtils.errorResponse(ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
        log.error("Has ERROR", ex);
        return HandleExceptionUtils.errorResponse(ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)   //(1)
    public Object exceptionHandler(IOException e) {
        if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(e), "Broken pipe")) {   //(2)
            return null;        //(2)	socket is closed, cannot return any response
        } else {
            return new HttpEntity<>(e.getMessage());  //(3)
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleException(MethodArgumentTypeMismatchException ex) {
        if (ex.getStackTrace() != null) {
            log.error("Has ERROR MethodArgumentTypeMismatchException with message = {}, at = {}",
                    ex.getMessage(), ex.getStackTrace()[0].toString());
        } else {
            log.error("Has ERROR MethodArgumentTypeMismatchException with message = {}", ex.getMessage());
        }
        return HandleExceptionUtils.errorResponse(I18n.getMessage(ErrorApp.BAD_REQUEST_PATH.getDescription()), null, HttpStatus.BAD_REQUEST);
    }
}
