package com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission;

import com.gdscplantry.plantry.domain.TrackerAndMission.vo.UserMissionVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    @Query(value = "select m.missionData.id from Mission m order by m.createdAt desc, m.missionData.type asc limit 6")
    ArrayList<Long> findAllOrderByCreatedAtWithJPQL();

    @Query(value = "select new com.gdscplantry.plantry.domain.TrackerAndMission.vo.UserMissionVo(m.id, m.missionData.title, case when a.id is null then true else false end ) " +
            "from Mission m left join AchievedMission a on m = a.mission " +
            "where (a.user = :user or a.id IS NULL) and m.createdAt >= :before and m.createdAt < :after " +
            "order by m.id ")
    ArrayList<UserMissionVo> findAllByUserAndCreatedAtWithJPQL(User user, LocalDateTime before, LocalDateTime after);
}