package com.gdscplantry.plantry.domain.Pantry.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
public class ProductListResDto {
    private String filter;
    private Map<Long, List<ProductListItemResDto>> result;
}
