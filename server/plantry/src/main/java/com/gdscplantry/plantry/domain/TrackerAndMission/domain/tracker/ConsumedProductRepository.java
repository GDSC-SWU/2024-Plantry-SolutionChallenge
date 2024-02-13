package com.gdscplantry.plantry.domain.TrackerAndMission.domain.tracker;

import com.gdscplantry.plantry.domain.TrackerAndMission.vo.ConsumptionDataVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface ConsumedProductRepository extends JpaRepository<ConsumedProduct, Long> {
    ArrayList<ConsumedProduct> findAllByUser(User user);

    void deleteAllByUser(User user);

    @Query(value = "select new com.gdscplantry.plantry.domain.TrackerAndMission.vo.ConsumptionDataVo(c.type, sum(c.count)) " +
            "from ConsumedProduct c where c.user = :user and c.createdAt >= :createdAt " +
            "group by c.type " +
            "order by case when c.type = 'Ingestion' then 0 " +
            "when c.type = 'Disposal' then 1 " +
            "when c.type = 'Sharing' then 2 " +
            "when c.type = 'Mistake' then 3 end ")
    ArrayList<ConsumptionDataVo> findAllByUserAndCreatedAtWithJPQL(User user, LocalDateTime createdAt);
}
