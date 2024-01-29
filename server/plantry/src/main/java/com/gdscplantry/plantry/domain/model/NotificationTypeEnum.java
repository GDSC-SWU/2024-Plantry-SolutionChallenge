package com.gdscplantry.plantry.domain.model;

import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationTypeEnum {
    EXP_DDAY(0,
            "[D-day] $name 🚀",
            "Today is the deadline for $pantry's $name!"),
    EXP_D1(1,
            "[D-1] $name 🚀",
            "$pantry's $name deadline is only 1 days left!"),
    EXP_D3(3,
            "[D-3] $name 🚀",
            "$pantry's $name deadline is only 3 days left!"),
    EXP_D7(7,
            "[D-7] $name 🚀",
            "$pantry's $name deadline is only 7 days left!"),
    PANTRY_SHARE_REQ(10, null, null),
    PANTRY_SHARE_RES(11, null, null),
    ;

    private final Integer key;
    private final String title;
    private final String body;
    private static final String addedBody = "\nWith the Use-by Date, We recommend you change it to D-$day.";

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

    public static String getAddedBody(Integer day) {
        return addedBody.replace("$day", day.toString());
    }

    public static String getTitleStr(String name, Integer key) {
        return findByKey(key).getTitle().replace("$name", name);
    }

    public static String getBodyStr(String pantry, String name, NotificationTypeEnum typeEnum, Boolean isNonUseByDate) {
        String body = typeEnum.getBody().replace("$pantry", pantry).replace("$name", name);
        String addedBodyWithDay = getAddedBody(typeEnum.getKey());
        if (isNonUseByDate)
            return body + addedBodyWithDay;
        else
            return body;
    }

    public static String getUpdatedBody(Integer day, String formerBody, Boolean isNonUseByDate) {
        if (!formerBody.contains("Use-by Date") && isNonUseByDate)
            return formerBody + getAddedBody(day);
        else if (formerBody.contains("Use-by Date") && !isNonUseByDate)
            return formerBody.split("\n")[0];
        else
            return formerBody;
    }
}
