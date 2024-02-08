package com.gdscplantry.plantry.domain.TrackerAndMission.service;

import com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission.MissionRepository;
import com.gdscplantry.plantry.domain.TrackerAndMission.dto.MissionListResDto;
import com.gdscplantry.plantry.domain.TrackerAndMission.error.TrackerAndMissionErrorCode;
import com.gdscplantry.plantry.domain.TrackerAndMission.vo.UserMissionVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionService {
    private final MissionRepository missionRepository;

    public MissionListResDto getMissionList(User user) {
        // Find recent mission update time
        LocalDateTime monday = LocalDateTime.now().with(LocalTime.of(0, 0));
        if (monday.getDayOfWeek() != DayOfWeek.MONDAY)
            monday = monday.minusDays(monday.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        LocalDateTime nextMonday = monday.plusWeeks(1);

        // Find data
        ArrayList<UserMissionVo> result = missionRepository.findAllByUserAndCreatedAtWithJPQL(user, monday, nextMonday);

        if (result.size() != 3)
            throw new AppException(TrackerAndMissionErrorCode.MISSION_SIZE_ERROR);

        return new MissionListResDto(result);
    }
}
