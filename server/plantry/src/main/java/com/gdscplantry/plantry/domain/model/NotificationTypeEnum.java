package com.gdscplantry.plantry.domain.model;

import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Getter
public enum NotificationTypeEnum {
    EXP_DDAY(0,
            "[D-day] $name 🚀",
            "Today is the deadline for $pantry's $name!",
            "Exp"),
    EXP_D1(1,
            "[D-1] $name 🚀",
            "$pantry's $name deadline is only 1 days left!",
            "Exp"),
    EXP_D3(3,
            "[D-3] $name 🚀",
            "$pantry's $name deadline is only 3 days left!",
            "Exp"),
    EXP_D7(7,
            "[D-7] $name 🚀",
            "$pantry's $name deadline is only 7 days left!",
            "Exp"),
    EXP_NON_DDAY(10,
            "[D-day] $name 🚀",
            "Today is the deadline for $pantry's $name!\nWith the Use-by Date, We recommend you change it to D$day.",
            "Exp"),
    EXP_NON_D1(11,
            "[D-1] $name 🚀",
            "$pantry's $name deadline is only 1 days left!\nWith the Use-by Date, We recommend you change it to D$day.",
            "Exp"),
    EXP_NON_D3(13,
            "[D-3] $name 🚀",
            "$pantry's $name deadline is only 3 days left!\nWith the Use-by Date, We recommend you change it to D$day.",
            "Exp"),
    EXP_NON_D7(17,
            "[D-7] $name 🚀",
            "$pantry's $name deadline is only 7 days left!\nWith the Use-by Date, We recommend you change it to D$day.",
            "Exp"),
    PANTRY_SHARE_REQ(20, null, null, "Pantry Share"),
    PANTRY_SHARE_RES(21, null, null, "Pantry Share"),
    ;

    private final Integer key;
    private final String title;
    private final String body;
    private final String typeStr;

    public static NotificationTypeEnum findByKey(Integer key) {
        return switch (key) {
            case 0 -> EXP_DDAY;
            case 1 -> EXP_D1;
            case 3 -> EXP_D3;
            case 7 -> EXP_D7;
            case 10 -> EXP_NON_DDAY;
            case 11 -> EXP_NON_D1;
            case 13 -> EXP_NON_D3;
            case 17 -> EXP_NON_D7;
            case 20 -> PANTRY_SHARE_REQ;
            case 21 -> PANTRY_SHARE_RES;
            default -> throw new AppException(PantryErrorCode.INVALID_DELETE_TYPE);
        };
    }

    public static String getTitleStr(String name, Integer key) {
        return findByKey(key).getTitle().replace("$name", name);
    }

    public static String getBodyStr(String pantry, String name, Integer typeKey, LocalDate date, LocalDate useByDateData) {
        String body = findByKey(typeKey).getBody().replace("$pantry", pantry).replace("$name", name);

        if (typeKey >= 10 && typeKey <= 17) {
            long dayLong = (date.until(useByDateData, ChronoUnit.DAYS) + (typeKey - 10)) * -1;
            return body.replace("$day", dayLong > 0 ? "+" + dayLong : String.valueOf(dayLong));
        } else
            return body;
    }
}
