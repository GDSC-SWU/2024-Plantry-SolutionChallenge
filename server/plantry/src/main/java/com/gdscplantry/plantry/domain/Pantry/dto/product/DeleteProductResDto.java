package com.gdscplantry.plantry.domain.Pantry.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
public class DeleteProductResDto {
    private Long id;
    private String type;
    private BigDecimal result;
}
