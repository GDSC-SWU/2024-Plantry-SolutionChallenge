package com.gdscplantry.plantry.domain.TrackerAndMission.vo;

import com.gdscplantry.plantry.domain.model.ProductDeleteTypeEnum;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ConsumptionDataVo {
    private ProductDeleteTypeEnum type;
    private BigDecimal value;

    public ConsumptionDataVo(ProductDeleteTypeEnum type, BigDecimal value) {
        this.type = type;
        this.value = value;
    }
}
