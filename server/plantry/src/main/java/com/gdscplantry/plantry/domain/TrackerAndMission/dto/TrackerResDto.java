package com.gdscplantry.plantry.domain.TrackerAndMission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class TrackerResDto {
    private final Map<String, Double> result;
}
