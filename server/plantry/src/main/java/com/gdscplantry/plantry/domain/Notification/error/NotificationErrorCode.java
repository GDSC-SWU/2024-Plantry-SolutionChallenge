package com.gdscplantry.plantry.domain.Notification.error;

import com.gdscplantry.plantry.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NotificationErrorCode implements ErrorCode {
    INVALID_PRODUCT_NOTIFICATION_REQ(HttpStatus.BAD_REQUEST, "Invalid notification list."),
    NOTIFICATION_ALREADY_CHECKED(HttpStatus.BAD_REQUEST, "Notification already checked."),
    NOTIFICATION_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Notification access denied."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Notification not found. (Invalid notification id)"),
    NOTIFICATION_SIZE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error - invalid notification data"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    NotificationErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
