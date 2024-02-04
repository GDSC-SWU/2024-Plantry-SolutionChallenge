package com.gdscplantry.plantry.domain.Notification.vo;

import com.gdscplantry.plantry.domain.model.NotificationTypeEnum;
import lombok.Getter;

@Getter
public class MessageVo {
    private final Long userId;
    private final String deviceToken;
    private final NotificationTypeEnum notificationType;
    private final String title;
    private final String body;
    private final Long entityId;

    public MessageVo(Long userId, String deviceToken, Integer typeKey, String title, String body, Long entityId) {
        this.userId = userId;
        this.deviceToken = deviceToken;
        this.notificationType = NotificationTypeEnum.findByKey(typeKey);
        this.title = title;
        this.body = body;
        this.entityId = entityId;
    }

    public MessageVo(Long userId, String deviceToken, Integer typeKey) {
        this.userId = userId;
        this.deviceToken = deviceToken;
        this.notificationType = NotificationTypeEnum.findByKey(typeKey);
        this.title = this.notificationType.getTitle();
        this.body = this.notificationType.getBody();
        this.entityId = null;
    }
}
