package com.gdscplantry.plantry.global.error.handler;

import com.gdscplantry.plantry.global.common.ResponseDto;
import com.gdscplantry.plantry.global.error.ErrorCode;
import com.gdscplantry.plantry.global.error.GlobalErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // ApiException
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseDto> exceptionHandler(AppException e) {
        int code = e.getErrorCode().getHttpStatus().value();
        String message = e.getMessage();

        return ResponseEntity.status(code).body(ResponseDto.of(code, message));
    }

    // MethodNotAllowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto> exceptionHandler(HttpRequestMethodNotSupportedException e) {
        ErrorCode errorCode = GlobalErrorCode.METHOD_NOT_ALLOWED;
        int code = errorCode.getHttpStatus().value();
        String message = errorCode.getMessage();

        return ResponseEntity.status(code).body(ResponseDto.of(code, message));
    }

    // InternalServerError
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> exceptionHandler(Exception e) {
        ErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        int code = errorCode.getHttpStatus().value();
        String message = errorCode.getMessage();

        e.printStackTrace();

        return ResponseEntity.status(code).body(ResponseDto.of(code, message));
    }
}
