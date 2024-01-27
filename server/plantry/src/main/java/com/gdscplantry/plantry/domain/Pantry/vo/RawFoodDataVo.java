package com.gdscplantry.plantry.domain.Pantry.vo;

import lombok.Getter;

import java.util.Objects;

@Getter
public class RawFoodDataVo {
    private final Long foodDataId;
    private final String emoji;
    private final Integer day;

    public RawFoodDataVo(Long foodDataId, String emoji, Integer value, Boolean isDay) {
        this.foodDataId = foodDataId;
        this.emoji = emoji;
        if (value != null && !isDay)
            this.day = value / 24;
        else this.day = Objects.requireNonNullElse(value, 0);
    }

    public RawFoodDataVo(Integer value, Boolean isDay) {
        this.foodDataId = null;
        this.emoji = null;
        if (!isDay)
            this.day = value / 24;
        else this.day = Objects.requireNonNullElse(value, 0);
    }
}
