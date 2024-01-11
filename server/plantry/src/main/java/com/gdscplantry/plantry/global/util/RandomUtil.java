package com.gdscplantry.plantry.global.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RandomUtil {
    static public String getRandomNickname() {
        String uuid = UUID.randomUUID().toString().replace("-", "");

        return "User" + uuid;
    }
}
