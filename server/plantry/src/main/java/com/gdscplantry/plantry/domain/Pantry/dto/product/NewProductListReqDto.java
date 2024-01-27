package com.gdscplantry.plantry.domain.Pantry.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewProductListReqDto {
    @NotNull(message = "Value required.")
    private ArrayList<NewProductReqDto> list;
}
