package com.gdscplantry.plantry.domain.TrackerAndMission.service;

import com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission.Mission;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission.MissionDataRepository;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission.MissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class ScheduledMissionServiceTest {
    @Autowired
    private ScheduledMissionService scheduledMissionService;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionDataRepository missionDataRepository;

    @Test
    @DisplayName("Set mission")
    void setMission() {
        // given
        ArrayList<Mission> missions = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            missions.add(new Mission(missionDataRepository.findById((long) i).orElse(null)));
        for (int i = 8; i < 10; i++)
            missions.add(new Mission(missionDataRepository.findById((long) i).orElse(null)));
        missionRepository.saveAll(missions);

        // when
        scheduledMissionService.setMission();

        // then
        ArrayList<Long> actual = missionRepository.findAllOrderByCreatedAtWithJPQL();
        for (long idx : actual) log.info(idx + " ");
        assertThat(actual.get(0)).as("Mission data update failed.").isEqualTo(10L);
    }

    @Test
    @DisplayName("Set mission without former data")
    void setMission_without_former() {
        // given, when
        scheduledMissionService.setMission();

        // then
        ArrayList<Long> actual = missionRepository.findAllOrderByCreatedAtWithJPQL();
        for (long idx : actual) log.info(idx + " ");
        assertThat(actual.size()).as("Mission data update failed.").isEqualTo(3);
    }
}