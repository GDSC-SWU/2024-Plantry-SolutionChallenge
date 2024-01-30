package com.gdscplantry.plantry.domain.Notification.domain;

import com.gdscplantry.plantry.domain.Notification.vo.ExpNotificationProductVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteAllByUser(User user);

    void deleteAllByEntityIdAndTypeKeyGreaterThanEqual(Long entityId, Integer typeKey);

    @Query(value = "select n " +
            "from Notification n left join Product p on n.entityId = p.id " +
            "where p.pantryId = :pantryId and n.typeKey < 20")
    ArrayList<Notification> findAllByPantryIdJoinProductWithJPQL(Long pantryId);

    void deleteAllByEntityIdAndTypeKeyLessThan(Long entityId, Integer typeKey);

    ArrayList<Notification> findAllByUserAndEntityIdAndTypeKeyGreaterThanEqual(User user, Long entityId, Integer typeKey);

    @Query(value = "select new com.gdscplantry.plantry.domain.Notification.vo.ExpNotificationProductVo(n, p) " +
            "from Notification n left join Product p on n.entityId = p.id " +
            "where n.user = :user and p.pantryId = :pantryId and n.typeKey < 20")
    ArrayList<ExpNotificationProductVo> findAllByUserAndPantryIdJoinProductWithJPQL(User user, Long pantryId);

    ArrayList<Notification> findAllByEntityIdAndTypeKeyLessThan(Long entityId, Integer typeKey);

    Boolean existsAllByUserAndEntityIdAndTypeKeyLessThan(User user, Long entityId, Integer typeKey);

}
