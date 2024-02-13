package com.gdscplantry.plantry.domain.Notification.service;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Notification.domain.NotificationRepository;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class ScheduledNotificationServiceTest {
    @Autowired
    private ScheduledNotificationService service;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";

    User user;
    Notification notification;

    @BeforeEach
    void addMockUser() {
        // Save user
        final String DEVICE_TOKEN = "cAPJbZgURlq_k-B6PhFU2w:APA91bEIGzmhvay1nc-EJazCexDmOOcSuxYo-zjRH8kGJrZiOHyI1fGNuRuKX9PsSwoSyTOISwy5SRDHxlKaIVjYVr9CaPqNQtQVbSAMEVbQ2O_kWXHH9w8QtSQLHt15Rs1J_tzESmYg";
        user = userRepository.save(User.builder()
                .email(EMAIL)
                .nickname(NICKNAME)
                .build());
        user.updateUser(null, DEVICE_TOKEN);
    }

    void addMockNotification(LocalDateTime notifiedAt) {
        notification = notificationRepository.save(Notification.builder()
                .user(user)
                .typeKey(0)
                .title(("[D-day] Apple 🚀"))
                .body("Today is the deadline for My Pantry's Apple!")
                .entityId(0L)
                .notifiedAt(notifiedAt)
                .isDeleted(false)
                .build());
    }

    @Test
    @DisplayName("Send scheduled messages")
    void sendScheduledMessages() {
        // given
        LocalDateTime notifiedAt = LocalDateTime.now().withSecond(0).withNano(0);
        addMockNotification(notifiedAt);

        // when, then
        service.sendScheduledMessages();
    }

    @Test
    @DisplayName("Delete outdated data")
    void deleteOutdatedData() {
        // given
        LocalDateTime notifiedAt = LocalDateTime.now().minusSeconds(30);
        addMockNotification(notifiedAt);

        // when
        service.deleteOutdatedData();

        // then
        Optional<Notification> actual = notificationRepository.findById(notification.getId());
        assertThat(actual).as("Notification save failed.").isNotEmpty();
        assertThat(actual.get().getIsDeleted()).as("Outdated data deletion failed.").isTrue();
    }
}