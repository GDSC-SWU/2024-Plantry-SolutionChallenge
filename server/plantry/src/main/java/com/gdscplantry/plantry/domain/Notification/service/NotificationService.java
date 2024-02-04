package com.gdscplantry.plantry.domain.Notification.service;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Notification.domain.NotificationRepository;
import com.gdscplantry.plantry.domain.Notification.dto.*;
import com.gdscplantry.plantry.domain.Notification.error.NotificationErrorCode;
import com.gdscplantry.plantry.domain.Pantry.domain.Product;
import com.gdscplantry.plantry.domain.Pantry.service.ProductService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ProductService productService;

    final int[] TYPE_KEYS = {0, 1, 3, 7};

    @Transactional(readOnly = true)
    public ProductNotificationListResDto readProductNotificationList(User user, Long productId) {
        // Find product & check access rights
        productService.validateProductId(user, productId);

        // Find result
        ArrayList<Integer> result = notificationRepository.findAllTypeByUserAndEntityIdAndTypeKeyWithJPQL(user, productId);

        // Err if result size over 4
        if (result.size() > 4)
            throw new AppException(NotificationErrorCode.NOTIFICATION_SIZE_ERROR);

        return new ProductNotificationListResDto(result);
    }

    public Boolean validateReqList(ArrayList<Integer> list) {
        return list.size() != list.stream().filter(key -> key.equals(0) || key.equals(1) || key.equals(3) || key.equals(7)).count();
    }

    @Transactional
    public ProductNotificationListResDto updateProductNotification(User user, Long productId, UpdateProductNotificationReqDto dto) {
        // Find product & check access rights
        Product product = productService.validateProductId(user, productId);

        // Validate req list
        ArrayList<Integer> list = dto.getList();
        Collections.sort(list);
        if (validateReqList(list))
            throw new AppException(NotificationErrorCode.INVALID_PRODUCT_NOTIFICATION_REQ);

        // Find all notifications of the product
        ArrayList<Notification> notifications = notificationRepository.findAllByEntityIdAndUserAndTypeKeyLessThan(productId, user, 20);

        if (list.size() == 0) {
            // If req list is empty
            for (Notification notification : notifications)
                notification.updateIsOff(true);
        } else {
            // Set notification off
            for (Notification notification : notifications) {
                int type = notification.getTypeKey() >= 10 ? notification.getTypeKey() - 10 : notification.getTypeKey();
                notification.updateIsOff(!list.contains(type));
            }
        }

        return new ProductNotificationListResDto(list);
    }

    @Transactional(readOnly = true)
    public NotificationListResDto readNotificationList(User user) {
        ArrayList<NotificationItemResDto> result = notificationRepository.findAllByUserWithJPQL(user);

        return new NotificationListResDto(result);
    }

    @Transactional
    public CheckNotificationResDto checkNotification(User user, Long notificationId) {
        // Validate notification id
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        // Err if notification user is not req user
        if (!notification.getUser().equals(user))
            throw new AppException(NotificationErrorCode.NOTIFICATION_ACCESS_DENIED);

        // Err if notification already checked
        if (notification.getIsChecked())
            throw new AppException(NotificationErrorCode.NOTIFICATION_ALREADY_CHECKED);

        // Update notification
        notification.updateIsChecked();

        return new CheckNotificationResDto(notificationId);
    }
}
