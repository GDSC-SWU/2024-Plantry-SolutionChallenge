package com.gdscplantry.plantry.domain.model;

import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationTypeEnum {
    EXP_DDAY(0),
    EXP_D1(1),
    EXP_D3(3),
    EXP_D7(7),
    PANTRY_SHARE_REQ(10),
    PANTRY_SHARE_RES(11),
    ;

    private final Integer key;

    public static NotificationTypeEnum findByKey(Integer key) {
        return switch (key) {
            case 0 -> EXP_DDAY;
            case 1 -> EXP_D1;
            case 3 -> EXP_D3;
            case 7 -> EXP_D7;
            case 10 -> PANTRY_SHARE_REQ;
            case 11 -> PANTRY_SHARE_RES;
            default -> throw new AppException(PantryErrorCode.INVALID_DELETE_TYPE);
        };
    }
}
