package com.gdscplantry.plantry.domain.TrackerAndMission.error;

import com.gdscplantry.plantry.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TrackerAndMissionErrorCode implements ErrorCode {
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "Mission not found. (Invalid mission id)"),
    MISSION_SIZE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error - Mission data read failed."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    TrackerAndMissionErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
