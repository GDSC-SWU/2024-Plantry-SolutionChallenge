package com.gdscplantry.plantry.domain.TrackerAndMission.vo;

import lombok.Getter;

@Getter
public class UserMissionVo {
    private final Long missionId;
    private final String content;
    private final Boolean isAchieved;

    public UserMissionVo(Long missionId, String content, Boolean isAchieved) {
        this.missionId = missionId;
        this.content = content;
        this.isAchieved = isAchieved;
    }
}
