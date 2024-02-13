package com.gdscplantry.plantry.domain.Pantry.vo;

import com.gdscplantry.plantry.domain.Pantry.domain.Pantry;
import lombok.Getter;

@Getter
public class PantryWithCodeVo {
    private Boolean isOwner;
    private Pantry pantry;

    public PantryWithCodeVo(Boolean isOwner, Pantry pantry) {
        this.isOwner = isOwner;
        this.pantry = pantry;
    }
}
