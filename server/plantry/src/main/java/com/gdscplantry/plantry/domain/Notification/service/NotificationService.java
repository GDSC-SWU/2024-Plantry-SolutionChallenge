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
import com.gdscplantry.plantry.global.util.FcmUtil;
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
public class NotificationService {
    private final FcmUtil fcmUtil;
    private final NotificationRepository notificationRepository;
    private final UserPantryRepository userPantryRepository;

    int[] typeKeys = {0, 1, 3, 7};
    int[] nonTypeKeys = {10, 11, 13, 17};

    public LocalDateTime getNotificationTime(LocalDate date, Integer typeKey, Integer userNotificationTime) {
        int days = typeKey >= 10 ? typeKey - 10 : typeKey;
        return LocalDateTime.of(date.minusDays(days), LocalTime.of(userNotificationTime, 0));
    }

    public Integer setType(boolean isNonUseByDate, Integer formerKey) {
        boolean isFormerNon = formerKey >= 10;
        if (isNonUseByDate == isFormerNon)
            return formerKey;
        else
            return isNonUseByDate ? formerKey + 10 : formerKey - 10;
    }

    @Transactional(readOnly = true)
    public ArrayList<Notification> addDefaultExpNotification(User user, Product product) {
        ArrayList<Notification> notificationArrayList = new ArrayList<>();

        // If use-by-date recommendation needed
        boolean isNonUseByDate = !product.getIsUseByDate() && product.getUseByDateData() != null;

        String pantryTitle = userPantryRepository.findByPantryIdAndUser(product.getPantryId(), user)
                .orElseThrow(() -> new AppException(PantryErrorCode.PANTRY_NOT_FOUND))
                .getTitle();

        int[] keys = isNonUseByDate ? nonTypeKeys : typeKeys;

        for (int key : keys) {
            LocalDate useByDate = isNonUseByDate ? product.getUseByDateData() : null;
            LocalDateTime notifiedAt = getNotificationTime(product.getDate(), key, user.getNotificationTime());
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

        if (notifications.size() == 0)
            return false;

        // Find pantry title
        String pantryTitle = userPantryRepository.findByPantryIdAndUser(product.getPantryId(), user)
                .orElseThrow(() -> new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR))
                .getTitle();

        // If use-by-date recommendation needed
        boolean isNonUseByDate = !product.getIsUseByDate() && product.getUseByDateData() != null;

        for (Notification notification : notifications) {
            LocalDate useByDate = isNonUseByDate ? product.getUseByDateData() : null;
            Integer newType = setType(isNonUseByDate, notification.getTypeKey());
            LocalDateTime notifiedAt = getNotificationTime(product.getDate(), newType, notification.getUser().getNotificationTime());
            String title = NotificationTypeEnum.getTitleStr(product.getName(), newType);
            String body = NotificationTypeEnum.getBodyStr(pantryTitle, product.getName(), newType, product.getDate(), useByDate);

            notification.updateNotification(newType, title, body, notifiedAt);
        }

        return true;
    }
}
