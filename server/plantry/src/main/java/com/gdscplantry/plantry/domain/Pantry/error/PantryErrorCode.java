package com.gdscplantry.plantry.domain.Pantry.error;

import com.gdscplantry.plantry.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PantryErrorCode implements ErrorCode {
    INVALID_DELETE_TYPE(HttpStatus.BAD_REQUEST, "Invalid delete type."),
    INVALID_COUNT(HttpStatus.BAD_REQUEST, "Invalid count value."),
    INVALID_DELETE_COUNT(HttpStatus.BAD_REQUEST, "Deletion failed. Result value is less than 0."),
    INVALID_STORAGE(HttpStatus.BAD_REQUEST, "Invalid storage value."),
    PANTRY_ALREADY_EXISTS(HttpStatus.FORBIDDEN, "Pantry already exists."),
    PANTRY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Pantry access denied."),
    PANTRY_CODE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Pantry code access denied."),
    PRODUCT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Product access denied."),
    PANTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "Pantry not found. (Invalid pantry id.)"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found. (Invalid product id.)"),
    INVALID_SHARE_CODE(HttpStatus.NOT_FOUND, "Pantry not found. (Invalid code)"),
    OWNER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error - Pantry owner not found."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    PantryErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
