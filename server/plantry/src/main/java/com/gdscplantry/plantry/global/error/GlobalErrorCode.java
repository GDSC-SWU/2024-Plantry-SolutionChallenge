package com.gdscplantry.plantry.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalErrorCode implements ErrorCode {
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "Invalid access."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "Authentication failed."),
    AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "Authorization failed."),
    ACCESS_TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "Access Token required."),
    REFRESH_TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "Refresh Token required."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "User not found."),
    LOGIN_REQUIRED(HttpStatus.FORBIDDEN, "Login required."),
    EXPIRED_JWT(HttpStatus.FORBIDDEN, "Token expired."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Request method is not supported."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");

    private final HttpStatus httpStatus;
    private final String message;

    GlobalErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
