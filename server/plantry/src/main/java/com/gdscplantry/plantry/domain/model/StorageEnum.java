package com.gdscplantry.plantry.domain.model;

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
            default -> Etc;
        };
    }
}
