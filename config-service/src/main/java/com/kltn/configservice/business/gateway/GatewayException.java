package com.kltn.configservice.business.gateway;

import lombok.Getter;

@Getter
public class GatewayException extends RuntimeException {

    private final String code;

    public GatewayException(String code, String message) {
        super(message);
        this.code = code;
    }

}