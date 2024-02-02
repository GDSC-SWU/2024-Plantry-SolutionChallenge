package com.gdscplantry.plantry.global.util;

import com.gdscplantry.plantry.domain.Notification.vo.MessageVo;
import com.gdscplantry.plantry.global.error.GlobalErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmUtil {
    private final FirebaseMessaging firebaseMessaging;

    public void validateToken(String deviceToken) {
        // Validate FCM device token
        RestTemplate restTemplate = new RestTemplate();
    }

    public void sendMessage(ArrayList<MessageVo> vos) {
        ArrayList<Message> messageList = new ArrayList<>();
        StringBuilder userList = new StringBuilder();

        for (MessageVo vo : vos) {
            Notification notification = Notification.builder()
                    .setTitle(vo.getTitle())
                    .setBody(vo.getBody())
                    .build();

            if (vo.getDeviceToken() == null) {
                log.error("FCM Token not found. - User " + vo.getUserId());
                continue;
            }

            messageList.add(Message.builder()
                    .setToken(vo.getDeviceToken())
                    .setNotification(notification)
                    .build()
            );

            userList.append(vo.getUserId()).append(" ");
        }

        try {
            if (messageList.isEmpty()) {
                log.info("[FCM] Sending completed successfully. " + LocalDateTime.now());
                log.info("*** target empty");
                return;
            }

            firebaseMessaging.sendEach(messageList);

            log.info("[FCM] Sending completed successfully. " + LocalDateTime.now());
            log.info("*** " + userList);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
            throw new AppException(GlobalErrorCode.FCM_SEND_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(GlobalErrorCode.FCM_SEND_FAILED);
        }
    }
}
