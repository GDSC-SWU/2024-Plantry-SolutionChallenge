package com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionDataRepository extends JpaRepository<MissionData, Long> {
}
