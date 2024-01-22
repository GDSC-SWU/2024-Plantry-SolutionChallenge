package com.gdscplantry.plantry.domain.Pantry.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
@Getter
public class NewProductListDto {
    private ArrayList<NewProductItemDto> result;
}
