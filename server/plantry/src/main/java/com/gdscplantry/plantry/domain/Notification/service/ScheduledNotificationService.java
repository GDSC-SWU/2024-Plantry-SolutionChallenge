package com.gdscplantry.plantry.domain.Notification.service;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Notification.domain.NotificationRepository;
import com.gdscplantry.plantry.domain.Notification.vo.MessageVo;
import com.gdscplantry.plantry.global.util.FcmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledNotificationService {
    private final NotificationRepository notificationRepository;
    private final FcmUtil fcmUtil;

    @Scheduled(cron = "0 0 * ? * *")
    @Transactional(readOnly = true)
    public void sendScheduledMessages() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        log.info("[FCM] Sending started. " + now);

        // Find notification data
        ArrayList<MessageVo> vos = notificationRepository.findAllByNotifiedAtWithJPQL(now);

        // Send FCM
        fcmUtil.sendMessage(vos);
    }

    @Scheduled(cron = "0 0 * ? * *")
    @Transactional
    public void deleteOutdatedData() {
        log.info("Outdated notification deletion started. " + LocalDateTime.now());
        StringBuilder result = new StringBuilder();

        // Delete notifications checked 7 days ago
        LocalDateTime outdatedNotifiedAt = LocalDateTime.now().minusDays(7);

        // Find data
        ArrayList<Notification> notifications = notificationRepository.findAllByNotifiedAtLessThanEqualAndIsChecked(outdatedNotifiedAt, true);

        // Set isDeleted true
        for (Notification notification : notifications) {
            notification.updateIsDeleted(true);
            result.append(notification.getId()).append(" ");
        }

        log.info("Outdated notification deletion completed. " + LocalDateTime.now());
        log.info("*** " + result);
    }

}