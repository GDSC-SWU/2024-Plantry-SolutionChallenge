package com.gdscplantry.plantry.domain.User.error;

import com.gdscplantry.plantry.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {
    ID_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "Id token required."),
    DEVICE_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "Device token required."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "Login failed."),
    INVALID_ID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid ID token."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
