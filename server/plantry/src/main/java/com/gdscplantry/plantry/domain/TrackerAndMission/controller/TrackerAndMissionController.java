package com.gdscplantry.plantry.domain.TrackerAndMission.controller;

import com.gdscplantry.plantry.domain.TrackerAndMission.dto.MissionAchieveResDto;
import com.gdscplantry.plantry.domain.TrackerAndMission.dto.MissionListResDto;
import com.gdscplantry.plantry.domain.TrackerAndMission.dto.TrackerResDto;
import com.gdscplantry.plantry.domain.TrackerAndMission.service.MissionService;
import com.gdscplantry.plantry.domain.TrackerAndMission.service.TrackerService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class TrackerAndMissionController {
    private final TrackerService trackerService;
    private final MissionService missionService;

    @GetMapping("/track")
    public ResponseEntity<ResponseDto> getTrackerResult(@RequestAttribute User user) {
        TrackerResDto trackerResDto = trackerService.getTrackerResult(user);

        return ResponseEntity.ok(DataResponseDto.of(trackerResDto, 200));
    }

    @GetMapping("/mission")
    public ResponseEntity<ResponseDto> getMissionList(@RequestAttribute User user) {
        MissionListResDto missionListResDto = missionService.getMissionList(user);

        return ResponseEntity.ok(DataResponseDto.of(missionListResDto, 200));
    }

    @PatchMapping("/mission")
    public ResponseEntity<ResponseDto> updateMissionAchieved(@RequestParam(value = "id") Long missionId, @RequestAttribute User user) {
        MissionAchieveResDto missionAchieveResDto = missionService.updateMissionAchieved(user, missionId);

        return ResponseEntity.status(201).body(DataResponseDto.of(missionAchieveResDto, 201));
    }
}
