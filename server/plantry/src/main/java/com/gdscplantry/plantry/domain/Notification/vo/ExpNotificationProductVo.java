package com.gdscplantry.plantry.domain.Notification.vo;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Pantry.domain.Product;
import lombok.Getter;

@Getter
public class ExpNotificationProductVo {
    private final Notification notification;
    private final Product product;

    public ExpNotificationProductVo(Notification notification, Product product) {
        this.notification = notification;
        this.product = product;
    }
}
