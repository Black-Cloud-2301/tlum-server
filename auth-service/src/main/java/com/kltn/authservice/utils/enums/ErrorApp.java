package com.kltn.authservice.utils.enums;

public enum ErrorApp {
    SUCCESS(200, "msg.status.success"),
    BAD_REQUEST(400, "msg.status.bad_request"),
    BAD_REQUEST_PATH(400, "msg.status.invalid_path"),
    UNAUTHORIZED(401, "msg.status.unauthorized"),
    FORBIDDEN(403, "msg.status.forbidden"),
    NOT_FOUND(404, "msg.status.not_found"),
    INTERNAL_SERVER(500, "msg.status.internal_server_error");

    private final int code;
    private final String description;

    ErrorApp(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}