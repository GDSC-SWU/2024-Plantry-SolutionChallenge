package com.gdscplantry.plantry.domain.Pantry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
@Getter
public class PantryListResDto {
    private ArrayList<PantryListItemDto> result;
}
