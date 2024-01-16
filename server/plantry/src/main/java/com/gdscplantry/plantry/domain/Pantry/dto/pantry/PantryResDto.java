package com.gdscplantry.plantry.domain.Pantry.dto.pantry;

import com.gdscplantry.plantry.domain.Pantry.domain.UserPantry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PantryResDto {
    private Long pantryId;
    private String title;
    private String color;

    public PantryResDto(UserPantry userPantry) {
        this.pantryId = userPantry.getPantryId();
        this.title = userPantry.getTitle();
        this.color = userPantry.getColor();
    }
}
