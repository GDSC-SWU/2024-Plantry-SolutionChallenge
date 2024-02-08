package com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission;

import com.gdscplantry.plantry.domain.model.MissionTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MissionDataRepository extends JpaRepository<MissionData, Long> {
    @Query(value = "select m from MissionData m where m.type = :type and m.id not in :ids")
    ArrayList<MissionData> findAllByIdWithJPQL(MissionTypeEnum type, ArrayList<Long> ids);
}
