package com.gdscplantry.plantry.domain.Pantry.dto.pantry;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PantryListItemDto {
    private Long id;
    private String title;
    private String color;
    private Boolean isMarked;

    public PantryListItemDto(Long id, String title, String color, Boolean isMarked) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.isMarked = isMarked;
    }
}
