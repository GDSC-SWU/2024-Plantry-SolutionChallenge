package com.gdscplantry.plantry.domain.Pantry.dto.pantry;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UpdatePantryReqDto {
    @Size(max = 12, message = "Invalid title value. (Max length is 12)")
    private String title;

    @Pattern(regexp = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Invalid color value.")
    private String color;
}
