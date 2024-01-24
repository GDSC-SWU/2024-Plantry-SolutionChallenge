package com.gdscplantry.plantry.domain.Pantry.error;

import com.gdscplantry.plantry.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PantryErrorCode implements ErrorCode {
    INVALID_COUNT(HttpStatus.BAD_REQUEST, "Invalid count value."),
    PANTRY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Pantry access denied."),
    PRODUCT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Product access denied."),
    PANTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "Pantry not found. (Invalid pantry id.)"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found. (Invalid product id.)"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    PantryErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
