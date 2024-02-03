package com.gdscplantry.plantry.domain.MyPage.error;

import com.gdscplantry.plantry.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MyPageErrorCode implements ErrorCode {
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Nickname already exists."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    MyPageErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
