package com.gdscplantry.plantry.domain.Notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class NotificationListResDto {
    private ArrayList<NotificationItemResDto> result;
}
