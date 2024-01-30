package com.gdscplantry.plantry.domain.Notification.domain;

import com.gdscplantry.plantry.domain.Notification.vo.ExpNotificationProductVo;
import com.gdscplantry.plantry.domain.Pantry.domain.*;
import com.gdscplantry.plantry.domain.Pantry.vo.FoodDataVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.model.NotificationTypeEnum;
import com.gdscplantry.plantry.domain.model.StorageEnum;
import com.gdscplantry.plantry.global.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class NotificationRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PantryRepository pantryRepository;

    @Autowired
    private UserPantryRepository userPantryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private final int[] TYPE_KEYS = {0, 1, 3, 7, 10, 11, 13, 17};
    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";
    final String COLOR = "A2E5B3";
    final String PANTRY_TITLE = "My pantry";
    final String PRODUCT_NAME1 = "Orange";
    final String PRODUCT_NAME2 = "Apple";

    User user;
    Pantry pantry;
    UserPantry userPantry;
    Product product1;
    Product product2;
    List<Notification> notifications1 = new ArrayList<>();
    List<Notification> notifications2 = new ArrayList<>();

    @BeforeEach
    void addMockData() {
        addMockUser();
        addMockProduct();
    }

    void addMockUser() {
        // Save user
        user = userRepository.save(User.builder()
                .email(EMAIL)
                .nickname(NICKNAME)
                .build());
    }

    void addMockProduct() {
        // Save pantry
        pantry = pantryRepository.save(new Pantry(RandomUtil.getUuid()));
        userPantry = userPantryRepository.save(UserPantry.builder()
                .user(user)
                .pantryId(pantry.getId())
                .title(PANTRY_TITLE)
                .color(COLOR)
                .build());

        // Save products
        product1 = productRepository.save(Product.builder()
                .pantryId(pantry.getId())
                .icon("🍊")
                .name(PRODUCT_NAME1)
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(true)
                .date(LocalDate.of(2024, 2, 1))
                .build());

        product2 = productRepository.save(Product.builder()
                .pantryId(pantry.getId())
                .icon("🍎")
                .name(PRODUCT_NAME2)
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(false)
                .date(LocalDate.of(2024, 2, 1))
                .build());
        product2.updateFoodData(FoodDataVo.builder()
                .value(10)
                .current(LocalDate.now())
                .foodDataId(1L)
                .build());
    }

    void addMockExpNotifications() {
        for (int i = 0; i < 4; i++) {
            LocalDateTime notifiedAt = LocalDateTime.of(product1.getDate().minusDays(TYPE_KEYS[i]), LocalTime.of(9, 0));
            notifications1.add(Notification.builder()
                    .user(user)
                    .typeKey(TYPE_KEYS[i])
                    .title(NotificationTypeEnum.getTitleStr(product1.getName(), TYPE_KEYS[i]))
                    .body(NotificationTypeEnum.getBodyStr(userPantry.getTitle(), product1.getName(), TYPE_KEYS[i], product1.getDate(), null))
                    .entityId(product1.getId())
                    .notifiedAt(notifiedAt)
                    .build());
        }

        for (int i = 0; i < 4; i++) {
            LocalDateTime notifiedAt = LocalDateTime.of(product2.getDate().minusDays(TYPE_KEYS[i]), LocalTime.of(9, 0));
            notifications2.add(Notification.builder()
                    .user(user)
                    .typeKey(TYPE_KEYS[i + 4])
                    .title(NotificationTypeEnum.getTitleStr(product2.getName(), TYPE_KEYS[i + 4]))
                    .body(NotificationTypeEnum.getBodyStr(userPantry.getTitle(), product2.getName(), TYPE_KEYS[i + 4], product2.getDate(), product2.getUseByDateData()))
                    .entityId(product2.getId())
                    .notifiedAt(notifiedAt)
                    .build());
        }

        notifications1 = notificationRepository.saveAll(notifications1);
        notifications2 = notificationRepository.saveAll(notifications2);
    }

    void addMockShareNotifications() {
        LocalDateTime notifiedAt = LocalDateTime.now();
        notifications1.add(Notification.builder()
                .user(user)
                .typeKey(20)
                .entityId(pantry.getId())
                .notifiedAt(notifiedAt)
                .build());
        notifications1.add(Notification.builder()
                .user(user)
                .typeKey(21)
                .entityId(pantry.getId())
                .notifiedAt(notifiedAt)
                .build());

        notifications1 = notificationRepository.saveAll(notifications1);
    }

    @Test
    @DisplayName("Delete all pantry-share notifications with pantry id")
    void deleteAllByEntityIdAndTypeKeyGreaterThanEqual() {
        // given
        addMockShareNotifications();

        // when
        notificationRepository.deleteAllByEntityIdAndTypeKeyGreaterThanEqual(pantry.getId(), 20);

        // then
        for (Notification notification : notifications1)
            assertThat(notificationRepository.findById(notification.getId())).as("Deletion failed.").isEmpty();
    }

    @Test
    @DisplayName("Find and delete all exp notifications with pantry id")
    void findAndDeleteAllByPantryIdJoinProductWithJPQL() {
        // given
        addMockExpNotifications();

        // when
        notificationRepository.deleteAll(notificationRepository.findAllByPantryIdJoinProductWithJPQL(pantry.getId()));

        // then
        for (Notification notification : notifications1)
            assertThat(notificationRepository.findById(notification.getId())).as("Deletion failed.").isEmpty();
        for (Notification notification : notifications2)
            assertThat(notificationRepository.findById(notification.getId())).as("Deletion failed.").isEmpty();
    }

    @Test
    @DisplayName("Delete all exp notifications with product id")
    void deleteAllByEntityIdAndTypeKeyLessThan() {
        // given
        addMockExpNotifications();

        // when
        notificationRepository.deleteAllByEntityIdAndTypeKeyLessThan(product1.getId(), 20);

        // then
        for (Notification notification : notifications1)
            assertThat(notificationRepository.findById(notification.getId())).as("Deletion failed.").isEmpty();
        for (Notification notification : notifications2)
            assertThat(notificationRepository.findById(notification.getId())).as("Wrong deletion.").isNotEmpty();
    }


    @Test
    @DisplayName("Find all pantry-share notifications with pantry id")
    void findAllByUserAndEntityIdAndTypeKeyGreaterThanEqual() {
        // given
        addMockShareNotifications();

        // when
        ArrayList<Notification> actual = notificationRepository.findAllByUserAndEntityIdAndTypeKeyGreaterThanEqual(user, pantry.getId(), 20);

        // then
        for (int i = 0; i < 2; i++) {
            assertThat(actual.get(i).getId()).as("Wrong data selected.").isEqualTo(notifications1.get(i).getId());
            assertThat(actual.get(i).getEntityId()).as("Wrong data selected.").isEqualTo(pantry.getId());
        }
    }

    @Test
    @DisplayName("Find all exp notifications with pantry id")
    void findAllByUserAndPantryIdJoinProductWithJPQL() {
        // given
        addMockExpNotifications();
        String expectedTitle0 = "[D-day] Orange 🚀";
        String expectedBody0 = "Today is the deadline for My pantry's Orange!";
        String expectedBody1 = "My pantry's Orange deadline is only 1 days left!";
        String expectedBody2 = "My pantry's Apple deadline is only 3 days left!\nWith the Use-by Date, We recommend you change it to D-11.";
        String expectedBody3 = "My pantry's Apple deadline is only 7 days left!\nWith the Use-by Date, We recommend you change it to D-15.";
        LocalDateTime expectedTime = LocalDateTime.of(2024, 2, 1, 9, 0);

        // when
        ArrayList<ExpNotificationProductVo> actual = notificationRepository.findAllByUserAndPantryIdJoinProductWithJPQL(user, pantry.getId());

        // then
        assertThat(actual.size()).as("Wrong notification size.").isEqualTo(8);
        assertThat(actual.get(0).getNotification().getTitle()).as("Wrong notification title.").isEqualTo(expectedTitle0);
        assertThat(actual.get(0).getNotification().getBody()).as("Wrong notification body.").isEqualTo(expectedBody0);
        assertThat(actual.get(0).getNotification().getEntityId()).as("Wrong entity id.").isEqualTo(product1.getId());
        assertThat(actual.get(0).getNotification().getTypeKey()).as("wrong notification type.").isEqualTo(TYPE_KEYS[0]);
        assertThat(actual.get(0).getNotification().getNotifiedAt()).as("Wrong notification time.").isEqualTo(expectedTime);
        assertThat(actual.get(0).getNotification().getIsChecked()).as("Wrong check status.").isFalse();
        assertThat(actual.get(1).getNotification().getBody()).as("Wrong notification body.").isEqualTo(expectedBody1);
        assertThat(actual.get(6).getNotification().getBody()).as("Wrong notification body.").isEqualTo(expectedBody2);
        assertThat(actual.get(7).getNotification().getBody()).as("Wrong notification body.").isEqualTo(expectedBody3);
    }

    @Test
    @DisplayName("Find all exp notifications with product id")
    void findAllByEntityIdAndTypeKeyLessThan() {
        // given
        addMockExpNotifications();
        String expectedTitle0 = "[D-day] Orange 🚀";
        String expectedBody0 = "Today is the deadline for My pantry's Orange!";
        String expectedBody1 = "My pantry's Orange deadline is only 1 days left!";
        String expectedBody2 = "My pantry's Orange deadline is only 3 days left!";
        String expectedBody3 = "My pantry's Orange deadline is only 7 days left!";
        LocalDateTime expectedTime = LocalDateTime.of(2024, 2, 1, 9, 0);

        // when
        ArrayList<Notification> actual = notificationRepository.findAllByEntityIdAndTypeKeyLessThan(product1.getId(), 20);

        // then
        assertThat(actual.size()).as("Wrong notification size.").isEqualTo(4);
        assertThat(actual.get(0).getTitle()).as("Wrong notification title.").isEqualTo(expectedTitle0);
        assertThat(actual.get(0).getBody()).as("Wrong notification body.").isEqualTo(expectedBody0);
        assertThat(actual.get(0).getEntityId()).as("Wrong entity id.").isEqualTo(product1.getId());
        assertThat(actual.get(0).getTypeKey()).as("wrong notification type.").isEqualTo(TYPE_KEYS[0]);
        assertThat(actual.get(0).getNotifiedAt()).as("Wrong notification time.").isEqualTo(expectedTime);
        assertThat(actual.get(0).getIsChecked()).as("Wrong check status.").isFalse();
        assertThat(actual.get(1).getBody()).as("Wrong notification body.").isEqualTo(expectedBody1);
        assertThat(actual.get(2).getBody()).as("Wrong notification body.").isEqualTo(expectedBody2);
        assertThat(actual.get(3).getBody()).as("Wrong notification body.").isEqualTo(expectedBody3);
    }

    @Test
    @DisplayName("If exp notification exists with product id")
    void existsAllByEntityIdAndTypeKeyLessThan() {
        // given
        addMockExpNotifications();

        // when
        boolean actual = notificationRepository.existsAllByUserAndEntityIdAndTypeKeyLessThan(user, product1.getId(), 20);

        // then
        assertThat(actual).as("Wrong existence").isTrue();
    }
}