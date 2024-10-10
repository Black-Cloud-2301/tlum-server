package com.kltn.individualservice.util.dto;


import com.kltn.individualservice.config.I18n;
import com.kltn.individualservice.constant.ErrorApp;
import com.kltn.individualservice.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {


    /**
     * Return data to client
     *
     * @param itemObject
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(Object itemObject) {
        BaseResponse baseResponse = new BaseResponse();
        if (itemObject != null) {
            baseResponse.setData(itemObject);
        }
        baseResponse.setCode(ErrorApp.SUCCESS.getCode());
        baseResponse.setMessage(I18n.getMessage(ErrorApp.SUCCESS.getDescription()));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    /**
     * Return data to client
     *
     * @param errorApp
     * @param itemObject
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(ErrorApp errorApp, Object itemObject) {
        BaseResponse baseResponse = new BaseResponse();
        if (itemObject != null) {
            baseResponse.setData(itemObject);
        }
        baseResponse.setCode(errorApp.getCode());
        baseResponse.setMessage(I18n.getMessage(errorApp.getDescription()));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    /**
     * Return mess to client
     *
     * @param code
     * @param description
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(int code, String description) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(code);
        baseResponse.setMessage(description);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }


    /**
     * Return mess to client
     *
     * @param code
     * @param description
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(int code, String description, HttpStatus httpStatus) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(code);
        baseResponse.setMessage(description);
        if (httpStatus == null) httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(baseResponse, httpStatus);
    }

    /**
     * Return mess to client
     *
     * @param errorApp
     * @param httpStatus
     * @return
     */
    public static ResponseEntity<Object> getResponseEntity(ErrorApp errorApp, HttpStatus httpStatus) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(errorApp.getCode());
        baseResponse.setMessage(I18n.getMessage(errorApp.getDescription()));
        if (httpStatus == null) httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(baseResponse, httpStatus);
    }
}
