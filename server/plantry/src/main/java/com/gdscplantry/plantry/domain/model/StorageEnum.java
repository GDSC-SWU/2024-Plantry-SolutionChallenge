package com.gdscplantry.plantry.domain.model;

import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StorageEnum {
    Cold("Cold"),
    Freeze("Freeze"),
    Etc("Etc"),
    ;

    private final String key;

    public static StorageEnum findByKey(String key) {
        return switch (key) {
            case "Cold" -> Cold;
            case "Freeze" -> Freeze;
            case "Etc" -> Etc;
            default -> throw new AppException(PantryErrorCode.INVALID_STORAGE);
        };
    }
}
