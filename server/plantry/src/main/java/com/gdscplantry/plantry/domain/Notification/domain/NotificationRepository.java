package com.gdscplantry.plantry.domain.Notification.domain;

import com.gdscplantry.plantry.domain.Notification.dto.NotificationItemResDto;
import com.gdscplantry.plantry.domain.Notification.vo.ExpNotificationProductVo;
import com.gdscplantry.plantry.domain.Notification.vo.MessageVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    ArrayList<Notification> findAllByUser(User user);

    void deleteAllByUser(User user);

    // Delete all pantry-share notifications with pantry id
    void deleteAllByEntityIdAndTypeKeyGreaterThanEqual(Long entityId, Integer typeKey);

    // Find all exp notifications with pantry id (for delete)
    @Query(value = "select n " +
            "from Notification n left join Product p on n.entityId = p.id " +
            "where p.pantryId = :pantryId and n.typeKey < 20")
    ArrayList<Notification> findAllByPantryIdJoinProductWithJPQL(Long pantryId);

    // Delete all exp notifications with product id
    void deleteAllByEntityIdAndTypeKeyLessThan(Long entityId, Integer typeKey);

    // Find all pantry-share notifications with pantry id
    ArrayList<Notification> findAllByUserAndEntityIdAndTypeKeyGreaterThanEqual(User user, Long entityId, Integer typeKey);

    // Find all exp notifications with pantry id
    @Query(value = "select new com.gdscplantry.plantry.domain.Notification.vo.ExpNotificationProductVo(n, p) " +
            "from Notification n left join Product p on n.entityId = p.id " +
            "where n.user = :user and p.pantryId = :pantryId and n.typeKey < 20")
    ArrayList<ExpNotificationProductVo> findAllByUserAndPantryIdJoinProductWithJPQL(User user, Long pantryId);

    // Find all exp notifications of the product with product id (Off included)
    ArrayList<Notification> findAllByEntityIdAndTypeKeyLessThan(Long entityId, Integer typeKey);

    // If user's exp notification exists with product id (Off excluded)
    Boolean existsAllByUserAndEntityIdAndIsOffAndTypeKeyLessThan(User user, Long entityId, boolean isOff, Integer typeKey);

    // Find user's notifications of the product with product id (off when isOff is false)
    @Query(value = "select case when n.typeKey >= 10 then n.typeKey - 10 else n.typeKey end " +
            "from Notification n " +
            "where n.user = :user and n.isOff = false and n.entityId = :productId and n.typeKey < 20 " +
            "order by n.typeKey asc")
    ArrayList<Integer> findAllTypeByUserAndEntityIdAndTypeKeyWithJPQL(User user, Long productId);

    // Find user's all exp notifications of the product with product id (Off included)
    ArrayList<Notification> findAllByEntityIdAndUserAndTypeKeyLessThan(Long entityId, User user, Integer typeKey);

    // Find user's sent notification list
    @Query(value = "select new com.gdscplantry.plantry.domain.Notification.dto.NotificationItemResDto(n) " +
            "from Notification n where n.user = :user and n.isOff = false and n.isDeleted = false and n.notifiedAt < current time " +
            "order by n.notifiedAt desc ")
    ArrayList<NotificationItemResDto> findAllByUserWithJPQL(User user);

    // Find all messages to send with notifiedAt
    @Query(value = "select new com.gdscplantry.plantry.domain.Notification.vo.MessageVo(u.id, u.deviceToken, n.typeKey, n.title, n.body, n.entityId) " +
            "from Notification n join User u on n.user = u " +
            "where n.notifiedAt = :notifiedAt and n.isOff = false and n.isDeleted = false ")
    ArrayList<MessageVo> findAllByNotifiedAtWithJPQL(LocalDateTime notifiedAt);

    // Find all outdated data with notifiedAt - 7days
    ArrayList<Notification> findAllByNotifiedAtLessThanEqualAndIsCheckedAndIsDeleted(LocalDateTime notifiedAt, Boolean isChecked, Boolean isDeleted);

    // Find user's all notifications not notified yet
    ArrayList<Notification> findAllByUserAndNotifiedAtIsGreaterThanEqual(User user, LocalDateTime notifiedAt);
}
