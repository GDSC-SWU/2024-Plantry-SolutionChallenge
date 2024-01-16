package com.gdscplantry.plantry.domain.Pantry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SetPantryMarkedResDto {
    private Long id;
    private Boolean isMarked;
}
