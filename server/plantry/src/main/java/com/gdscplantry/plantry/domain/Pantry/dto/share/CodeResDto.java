package com.gdscplantry.plantry.domain.Pantry.dto.share;

import lombok.Getter;

@Getter
public class CodeResDto {
    private final Long pantryId;
    private final String code;

    public CodeResDto(Long pantryId, String code) {
        this.pantryId = pantryId;
        this.code = code;
    }
}
