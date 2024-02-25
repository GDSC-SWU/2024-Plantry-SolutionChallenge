package com.gdscplantry.plantry.domain.Notification.dto;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.model.NotificationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@Builder
public class NotificationItemResDto {
    private Long id;
    private String type;
    private String title;
    private String body;
    private String notifiedAt;
    private Boolean isChecked;

    public NotificationItemResDto(Notification notification) {
        this.id = notification.getId();
        this.type = NotificationTypeEnum.findByKey(notification.getTypeKey()).getTypeStr();
        this.title = notification.getTitle();
        this.body = notification.getBody();
        this.notifiedAt = notification.getNotifiedAt().format(DateTimeFormatter.ISO_DATE_TIME);
        this.isChecked = notification.getIsChecked();
    }
}
