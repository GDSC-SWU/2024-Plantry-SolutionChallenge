package com.gdscplantry.plantry.domain.Pantry.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FoodDataVo {
    private final Long foodDataId;
    private final String emoji;
    private final LocalDate useByDateData;

    @Builder
    public FoodDataVo(Long foodDataId, String emoji, Integer value, LocalDate current) {
        this.foodDataId = foodDataId;
        this.emoji = emoji;
        if (value == null)
            this.useByDateData = null;
        else
            this.useByDateData = current.plusDays(value - 1);

    }
}
