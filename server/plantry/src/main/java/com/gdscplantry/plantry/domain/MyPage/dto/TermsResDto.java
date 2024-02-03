package com.gdscplantry.plantry.domain.MyPage.dto;

import com.gdscplantry.plantry.domain.MyPage.vo.TermsItemVo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class TermsResDto {
    private String type;
    private ArrayList<TermsItemVo> result;
}
