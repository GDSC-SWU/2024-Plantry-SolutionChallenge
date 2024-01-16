package com.gdscplantry.plantry.global.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RandomUtil {
    static public String getRandomNickname() {
        StringBuilder nickname = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 8; i++)
            nickname.append(chars.charAt((int) (Math.random() * chars.length())));

        return nickname.toString();
    }

    static public String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
