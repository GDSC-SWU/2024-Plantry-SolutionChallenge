package com.gdscplantry.plantry.domain.Pantry.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;

@Getter
@AllArgsConstructor
public class ProductListResultDto {
    private Long day;
    private LinkedList<ProductListItemDto> list;
}
