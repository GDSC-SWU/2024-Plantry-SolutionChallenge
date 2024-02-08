package com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    @Query(value = "select m.missionData.id from Mission m order by m.createdAt desc, m.missionData.type asc limit 6")
    ArrayList<Long> findAllOrderByCreatedAtWithJPQL();
}