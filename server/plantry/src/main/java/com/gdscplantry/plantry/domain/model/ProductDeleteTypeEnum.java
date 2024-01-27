package com.gdscplantry.plantry.domain.model;

import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductDeleteTypeEnum {
    Ingestion(1, "Ingestion"),
    Disposal(2, "Disposal"),
    Sharing(3, "Sharing"),
    Mistake(4, "Mistake");

    private final Integer key;
    private final String title;

    public static ProductDeleteTypeEnum findByKey(Integer key) {
        return switch (key) {
            case 1 -> Ingestion;
            case 2 -> Disposal;
            case 3 -> Sharing;
            case 4 -> Mistake;
            default -> throw new AppException(PantryErrorCode.INVALID_DELETE_TYPE);
        };
    }
}
