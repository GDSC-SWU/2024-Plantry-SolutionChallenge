package com.gdscplantry.plantry.domain.TrackerAndMission.service;

import com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission.Mission;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission.MissionData;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission.MissionDataRepository;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission.MissionRepository;
import com.gdscplantry.plantry.domain.model.MissionTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledMissionService {
    private final MissionRepository missionRepository;
    private final MissionDataRepository missionDataRepository;

    // Set new mission for this week on every 00:00 Mon
    @Scheduled(cron = "0 0 0 ? * MON")
    public void setMission() {
        log.info("[Mission] Data updating started - " + LocalDateTime.now());

        // Find data up to the past two weeks
        ArrayList<Long> lastMissions = missionRepository.findAllOrderByCreatedAtWithJPQL();

        // Select all missions not chosen in last 2 weeks
        ArrayList<MissionData> missionData1 = lastMissions.size() == 0 ?
                missionDataRepository.findAllByType(MissionTypeEnum.SINGLE) : missionDataRepository.findAllByIdWithJPQL(MissionTypeEnum.SINGLE, lastMissions);
        ArrayList<MissionData> missionData2 = lastMissions.size() == 0 ?
                missionDataRepository.findAllByType(MissionTypeEnum.GROUP) : missionDataRepository.findAllByIdWithJPQL(MissionTypeEnum.GROUP, lastMissions);

        // Find 3 random indexes
        ArrayList<Integer> idx = new ArrayList<>();
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < 2; i++) {
            int value = rand.nextInt(missionData1.size());
            if (!idx.contains(value))
                idx.add(value);
        }
        MissionData type2Mission = missionData2.get(rand.nextInt(missionData2.size()));

        // Add data
        ArrayList<Mission> result = new ArrayList<>();
        for (int i : idx)
            result.add(new Mission(missionData1.get(i)));
        result.add(new Mission(type2Mission));

        missionRepository.saveAll(result);

        log.info("[Mission] Data updating completed - " + LocalDateTime.now());
    }
}
