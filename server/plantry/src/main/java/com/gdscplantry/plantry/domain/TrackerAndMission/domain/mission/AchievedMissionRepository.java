package com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission;

import com.gdscplantry.plantry.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievedMissionRepository extends JpaRepository<AchievedMission, Long> {
    Optional<AchievedMission> findByMissionAndUser(Mission mission, User user);

    void deleteAllByUser(User user);
}
