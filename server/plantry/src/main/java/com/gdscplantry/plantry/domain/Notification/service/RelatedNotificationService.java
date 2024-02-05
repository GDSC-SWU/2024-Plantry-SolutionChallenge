package com.gdscplantry.plantry.domain.Notification.service;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Notification.domain.NotificationRepository;
import com.gdscplantry.plantry.domain.Notification.vo.ExpNotificationProductVo;
import com.gdscplantry.plantry.domain.Pantry.domain.Product;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantry;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.model.NotificationTypeEnum;
import com.gdscplantry.plantry.global.error.GlobalErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import com.gdscplantry.plantry.global.util.NotificationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelatedNotificationService {
    private final NotificationRepository notificationRepository;
    private final UserPantryRepository userPantryRepository;

    final int[] TYPE_KEYS = {0, 1, 3, 7};
    final int[] NON_TYPE_KEYS = {10, 11, 13, 17};

    @Transactional(readOnly = true)
    public ArrayList<Notification> addDefaultExpNotification(User user, Product product) {
        ArrayList<Notification> notificationArrayList = new ArrayList<>();

        // If use-by-date recommendation needed
        boolean isNonUseByDate = !product.getIsUseByDate() && product.getUseByDateData() != null;

        String pantryTitle = userPantryRepository.findByPantryIdAndUser(product.getPantryId(), user)
                .orElseThrow(() -> new AppException(PantryErrorCode.PANTRY_NOT_FOUND))
                .getTitle();

        int[] keys = isNonUseByDate ? NON_TYPE_KEYS : TYPE_KEYS;

        for (int key : keys) {
            LocalDate useByDate = isNonUseByDate ? product.getUseByDateData() : null;
            LocalDateTime notifiedAt = NotificationUtil.getNotificationTime(product.getDate(), key, user.getNotificationTime());
            if (notifiedAt.isAfter(LocalDateTime.now()))
                notificationArrayList.add(Notification.builder()
                        .user(user)
                        .typeKey(key)
                        .title(NotificationTypeEnum.getTitleStr(product.getName(), key))
                        .body(NotificationTypeEnum.getBodyStr(pantryTitle, product.getName(), key, product.getDate(), useByDate))
                        .entityId(product.getId())
                        .notifiedAt(notifiedAt)
                        .build());
        }

        return notificationArrayList;
    }

    @Transactional
    public void updatePantry(User user, UserPantry userPantry) {
        ArrayList<Notification> shareNotifications = notificationRepository.findAllByUserAndEntityIdAndTypeKeyGreaterThanEqual(user, userPantry.getPantryId(), 20);
        ArrayList<ExpNotificationProductVo> expNotifications = notificationRepository.findAllByUserAndPantryIdJoinProductWithJPQL(user, userPantry.getPantryId());

        // Update pantry name
        String pantryName = userPantry.getTitle();
        for (ExpNotificationProductVo vo : expNotifications) {
            LocalDate useByDate = !vo.getProduct().getIsUseByDate() && vo.getProduct().getUseByDateData() != null ? vo.getProduct().getUseByDateData() : null;
            Notification notification = vo.getNotification();
            Product product = vo.getProduct();
            notification.updateBody(NotificationTypeEnum.getBodyStr(pantryName, product.getName(), notification.getTypeKey(), vo.getProduct().getDate(), useByDate));
        }
    }

    @Transactional
    public Boolean updateProduct(User user, Product product) {
        ArrayList<Notification> notifications = notificationRepository.findAllByEntityIdAndTypeKeyLessThan(product.getId(), 20);

        // Find pantry title
        String pantryTitle = userPantryRepository.findByPantryIdAndUser(product.getPantryId(), user)
                .orElseThrow(() -> new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR))
                .getTitle();

        // If use-by-date recommendation needed
        boolean isNonUseByDate = !product.getIsUseByDate() && product.getUseByDateData() != null;

        for (Notification notification : notifications) {
            LocalDate useByDate = isNonUseByDate ? product.getUseByDateData() : null;
            Integer newType = NotificationUtil.setType(isNonUseByDate, notification.getTypeKey());
            LocalDateTime notifiedAt = NotificationUtil.getNotificationTime(product.getDate(), newType, notification.getUser().getNotificationTime());
            String title = NotificationTypeEnum.getTitleStr(product.getName(), newType);
            String body = NotificationTypeEnum.getBodyStr(pantryTitle, product.getName(), newType, product.getDate(), useByDate);

            notification.updateNotification(newType, title, body, notifiedAt);
            notification.updateIsDeleted(notifiedAt.isBefore(LocalDateTime.now()));
        }

        return notificationRepository.existsAllByUserAndEntityIdAndIsOffAndTypeKeyLessThan(user, product.getId(), false, 20);
    }

    @Transactional
    public void updateNotificationTime(User user, Integer time) {
        // Find all notifications not notified yet
        LocalDateTime now = LocalDateTime.now();
        ArrayList<Notification> notifications = notificationRepository.findAllByUserAndNotifiedAtIsGreaterThanEqual(user, now);

        // Update data
        for (Notification notification : notifications)
            notification.updateNotifiedAt(notification.getNotifiedAt().withHour(time));
    }

    @Transactional
    public void sharePantry(User owner, User user, Long pantryId) {
        // Find all notifications
        ArrayList<ExpNotificationProductVo> ownerNotifications = notificationRepository.findAllByUserAndPantryIdJoinProductWithJPQL(owner, pantryId);
        ArrayList<Notification> notifications = new ArrayList<>();

        // Add notifications
        for (ExpNotificationProductVo vo : ownerNotifications) {
            Notification notification = vo.getNotification();
            notifications.add(Notification.builder()
                    .user(user)
                    .typeKey(notification.getTypeKey())
                    .title(notification.getTitle())
                    .body(notification.getBody())
                    .entityId(notification.getEntityId())
                    .notifiedAt(LocalDateTime.of(notification.getNotifiedAt().toLocalDate(), LocalTime.of(user.getNotificationTime(), 0)))
                    .build());
        }
        
        // Save data
        notificationRepository.saveAll(notifications);
    }
}
