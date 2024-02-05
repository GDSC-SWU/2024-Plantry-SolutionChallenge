package com.gdscplantry.plantry.domain.Pantry.dto.share;

import com.gdscplantry.plantry.domain.Pantry.vo.PantryMemberVo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class PantryMemberResDto {
    private Boolean isUserOwner;
    private ArrayList<PantryMemberVo> list;
}
