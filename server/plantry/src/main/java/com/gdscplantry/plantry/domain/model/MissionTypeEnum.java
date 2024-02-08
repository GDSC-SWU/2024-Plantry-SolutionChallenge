package com.gdscplantry.plantry.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MissionTypeEnum {
    SINGLE(0, "Single"),
    GROUP(1, "Group"),
    ;

    private final Integer key;
    private final String title;
}