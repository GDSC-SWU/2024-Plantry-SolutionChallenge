package com.gdscplantry.plantry.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationUtil {
    public static LocalDateTime getNotificationTime(LocalDate date, Integer typeKey, Integer userNotificationTime) {
        int days = typeKey >= 10 ? typeKey - 10 : typeKey;
        return LocalDateTime.of(date.minusDays(days), LocalTime.of(userNotificationTime, 0));
    }

    public static Integer setType(boolean isNonUseByDate, Integer formerKey) {
        boolean isFormerNon = formerKey >= 10;
        if (isNonUseByDate == isFormerNon)
            return formerKey;
        else
            return isNonUseByDate ? formerKey + 10 : formerKey - 10;
    }
}
