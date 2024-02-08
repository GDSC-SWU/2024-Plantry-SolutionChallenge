package com.gdscplantry.plantry.domain.TrackerAndMission.dto;

import com.gdscplantry.plantry.domain.TrackerAndMission.vo.UserMissionVo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class MissionListResDto {
    private ArrayList<UserMissionVo> result;
}
