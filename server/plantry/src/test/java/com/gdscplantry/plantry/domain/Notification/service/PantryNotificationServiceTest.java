package com.gdscplantry.plantry.domain.Notification.service;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Notification.domain.NotificationRepository;
import com.gdscplantry.plantry.domain.Notification.vo.ExpNotificationProductVo;
import com.gdscplantry.plantry.domain.Pantry.domain.*;
import com.gdscplantry.plantry.domain.Pantry.dto.pantry.UpdatePantryReqDto;
import com.gdscplantry.plantry.domain.Pantry.dto.product.UpdateProductReqDto;
import com.gdscplantry.plantry.domain.Pantry.vo.FoodDataVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.model.NotificationTypeEnum;
import com.gdscplantry.plantry.domain.model.StorageEnum;
import com.gdscplantry.plantry.global.util.NotificationUtil;
import com.gdscplantry.plantry.global.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class PantryNotificationServiceTest {
    @Autowired
    private PantryNotificationService pantryNotificationService;

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
    ArrayList<Notification> notifications1 = new ArrayList<>();
    ArrayList<Notification> notifications2 = new ArrayList<>();

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

    void addMockNotifications() {
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

        notificationRepository.saveAll(notifications1);
        notificationRepository.saveAll(notifications2);
    }

    @Test
    @DisplayName("Calculate notifiedAt - Success")
    void getNotificationTime() {
        // given
        LocalDate date = LocalDate.of(2024, 2, 10);
        int userNotificationTime = 20;
        LocalDateTime[] expectedList = new LocalDateTime[8];

        for (int i = 0; i < 8; i++) {
            int days = TYPE_KEYS[i] >= 10 ? TYPE_KEYS[i] - 10 : TYPE_KEYS[i];
            expectedList[i] = LocalDateTime.of(2024, 2, 10 - days, 20, 0);
        }

        // when
        LocalDateTime[] actualList = new LocalDateTime[8];
        for (int i = 0; i < 8; i++)
            actualList[i] = NotificationUtil.getNotificationTime(date, TYPE_KEYS[i], userNotificationTime);

        // then
        for (int i = 0; i < 8; i++)
            assertThat(actualList[i]).as("NotifiedAt calculating failed.").isEqualTo(expectedList[i]);
    }

    @Test
    @DisplayName("Set notification type enum key when isNonUseByDate is true - Success")
    void setType_true() {
        // given
        boolean isNonUseByDate = true;
        int[] expected = {10, 11, 13, 17, 10, 11, 13, 17};

        // when
        int[] actual = new int[8];
        for (int i = 0; i < 8; i++)
            actual[i] = NotificationUtil.setType(isNonUseByDate, expected[i]);

        // then
        for (int i = 0; i < 8; i++)
            assertThat(actual[i]).as("Setting notification type failed.").isEqualTo(expected[i]);
    }

    @Test
    @DisplayName("Set notification type enum key when isNonUseByDate is false - Success")
    void setType_false() {
        // given
        boolean isNonUseByDate = false;
        int[] expected = {0, 1, 3, 7, 0, 1, 3, 7};

        // when
        int[] actual = new int[8];
        for (int i = 0; i < 8; i++)
            actual[i] = NotificationUtil.setType(isNonUseByDate, expected[i]);

        // then
        for (int i = 0; i < 8; i++)
            assertThat(actual[i]).as("Setting notification type failed.").isEqualTo(expected[i]);
    }

    @Test
    @DisplayName("Add default notifications when new product added - Success")
    void addDefaultExpNotification() {
        // given
        addMockUser();
        addMockProduct();
        String expectedTitle0 = "[D-day] Orange 🚀";
        String expectedBody0 = "Today is the deadline for My pantry's Orange!";
        LocalDateTime expectedTime = LocalDateTime.of(2024, 2, 1, 9, 0);

        // when
        ArrayList<Notification> actual = pantryNotificationService.addDefaultExpNotification(user, product1);

        // then
        assertThat(actual.size()).as("Wrong notification size.").isEqualTo(4);
        assertThat(actual.get(0).getTitle()).as("Wrong notification title.").isEqualTo(expectedTitle0);
        assertThat(actual.get(0).getBody()).as("Wrong notification body.").isEqualTo(expectedBody0);
        assertThat(actual.get(0).getEntityId()).as("Wrong entity id.").isEqualTo(product1.getId());
        assertThat(actual.get(0).getTypeKey()).as("wrong notification type.").isEqualTo(TYPE_KEYS[0]);
        assertThat(actual.get(0).getNotifiedAt()).as("Wrong notification time.").isEqualTo(expectedTime);
        assertThat(actual.get(0).getIsChecked()).as("Wrong check status.").isFalse();
    }


    @Test
    @DisplayName("Add default notifications when new product (NonUseByDate) added - Success")
    void addDefaultExpNotification_nonUseByDate() {
        // given
        addMockUser();
        addMockProduct();
        String expectedTitle0 = "[D-day] Apple 🚀";
        String expectedBody0 = "Today is the deadline for My pantry's Apple!\nWith the Use-by Date, We recommend you change it to D-8.";
        String expectedBody1 = "My pantry's Apple deadline is only 1 days left!\nWith the Use-by Date, We recommend you change it to D-9.";
        String expectedBody2 = "My pantry's Apple deadline is only 3 days left!\nWith the Use-by Date, We recommend you change it to D-11.";
        String expectedBody3 = "My pantry's Apple deadline is only 7 days left!\nWith the Use-by Date, We recommend you change it to D-15.";
        LocalDateTime expectedTime = LocalDateTime.of(2024, 2, 1, 9, 0);

        // when
        ArrayList<Notification> actual = pantryNotificationService.addDefaultExpNotification(user, product2);

        // then
        assertThat(actual.size()).as("Wrong notification size.").isEqualTo(4);
        assertThat(actual.get(0).getTitle()).as("Wrong notification title.").isEqualTo(expectedTitle0);
        assertThat(actual.get(0).getBody()).as("Wrong notification body.").isEqualTo(expectedBody0);
        assertThat(actual.get(0).getEntityId()).as("Wrong entity id.").isEqualTo(product2.getId());
        assertThat(actual.get(0).getTypeKey()).as("wrong notification type.").isEqualTo(TYPE_KEYS[4]);
        assertThat(actual.get(0).getNotifiedAt()).as("Wrong notification time.").isEqualTo(expectedTime);
        assertThat(actual.get(0).getIsChecked()).as("Wrong check status.").isFalse();
        assertThat(actual.get(1).getBody()).as("Wrong notification body.").isEqualTo(expectedBody1);
        assertThat(actual.get(2).getBody()).as("Wrong notification body.").isEqualTo(expectedBody2);
        assertThat(actual.get(3).getBody()).as("Wrong notification body.").isEqualTo(expectedBody3);
    }

    @Test
    @DisplayName("Update notifications when pantry updated - Success")
    void updatePantry() {
        // given
        addMockUser();
        addMockProduct();
        addMockNotifications();
        UpdatePantryReqDto updatePantryReqDto = new UpdatePantryReqDto("Refrig1", userPantry.getColor());
        userPantry.updatePantry(updatePantryReqDto);
        String expectedTitle0 = "[D-day] Orange 🚀";
        String expectedBody0 = "Today is the deadline for Refrig1's Orange!";
        String expectedBody1 = "Refrig1's Orange deadline is only 1 days left!";
        String expectedBody2 = "Refrig1's Apple deadline is only 3 days left!\nWith the Use-by Date, We recommend you change it to D-11.";
        String expectedBody3 = "Refrig1's Apple deadline is only 7 days left!\nWith the Use-by Date, We recommend you change it to D-15.";
        LocalDateTime expectedTime = LocalDateTime.of(2024, 2, 1, 9, 0);

        // when
        pantryNotificationService.updatePantry(user, userPantry);

        // then
        ArrayList<ExpNotificationProductVo> actual = notificationRepository.findAllByUserAndPantryIdJoinProductWithJPQL(user, userPantry.getPantryId());
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
    @DisplayName("Update notifications when product updated - Success")
    void updateProduct() {
        // given
        addMockUser();
        addMockProduct();
        addMockNotifications();
        UpdateProductReqDto updateProductReqDto = new UpdateProductReqDto(product1.getIcon(), "Apple juice", product1.getIsUseByDate(), product1.getDate().format(DateTimeFormatter.ISO_DATE), product1.getStorage().getKey(), product1.getCount().toString());
        product1.updateProduct(updateProductReqDto.toEntity());
        String expectedTitle0 = "[D-day] Apple juice 🚀";
        String expectedBody0 = "Today is the deadline for My pantry's Apple juice!";
        String expectedBody1 = "My pantry's Apple juice deadline is only 1 days left!";
        String expectedBody2 = "My pantry's Apple juice deadline is only 3 days left!";
        String expectedBody3 = "My pantry's Apple juice deadline is only 7 days left!";
        LocalDateTime expectedTime = LocalDateTime.of(2024, 2, 1, 9, 0);

        // when
        pantryNotificationService.updateProduct(user, product1);

        // then
        ArrayList<Notification> actual = notificationRepository.findAllByEntityIdAndTypeKeyLessThan(product1.getId(), 20);
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
    @DisplayName("Update notifications when product's deadline updated - Success")
    void updateProduct_date() {
        // given
        addMockUser();
        addMockProduct();
        addMockNotifications();
        UpdateProductReqDto updateProductReqDto = new UpdateProductReqDto(product2.getIcon(), product2.getName(), product2.getIsUseByDate(), "2024-02-25", product2.getStorage().getKey(), product2.getCount().toString());
        product2.updateProduct(updateProductReqDto.toEntity());
        String expectedTitle0 = "[D-day] Apple 🚀";
        String expectedBody0 = "Today is the deadline for My pantry's Apple!\nWith the Use-by Date, We recommend you change it to D+16.";
        String expectedBody1 = "My pantry's Apple deadline is only 1 days left!\nWith the Use-by Date, We recommend you change it to D+15.";
        String expectedBody2 = "My pantry's Apple deadline is only 3 days left!\nWith the Use-by Date, We recommend you change it to D+13.";
        String expectedBody3 = "My pantry's Apple deadline is only 7 days left!\nWith the Use-by Date, We recommend you change it to D+9.";
        LocalDateTime expectedTime0 = LocalDateTime.of(2024, 2, 25, 9, 0);
        LocalDateTime expectedTime1 = LocalDateTime.of(2024, 2, 24, 9, 0);
        LocalDateTime expectedTime2 = LocalDateTime.of(2024, 2, 22, 9, 0);
        LocalDateTime expectedTime3 = LocalDateTime.of(2024, 2, 18, 9, 0);

        // when
        pantryNotificationService.updateProduct(user, product2);

        // then
        ArrayList<Notification> actual = notificationRepository.findAllByEntityIdAndTypeKeyLessThan(product2.getId(), 20);
        assertThat(actual.size()).as("Wrong notification size.").isEqualTo(4);
        assertThat(actual.get(0).getTitle()).as("Wrong notification title.").isEqualTo(expectedTitle0);
        assertThat(actual.get(0).getBody()).as("Wrong notification body.").isEqualTo(expectedBody0);
        assertThat(actual.get(0).getEntityId()).as("Wrong entity id.").isEqualTo(product2.getId());
        assertThat(actual.get(0).getTypeKey()).as("wrong notification type.").isEqualTo(TYPE_KEYS[4]);
        assertThat(actual.get(0).getNotifiedAt()).as("Wrong notification time.").isEqualTo(expectedTime0);
        assertThat(actual.get(0).getIsChecked()).as("Wrong check status.").isFalse();
        assertThat(actual.get(1).getBody()).as("Wrong notification body.").isEqualTo(expectedBody1);
        assertThat(actual.get(2).getBody()).as("Wrong notification body.").isEqualTo(expectedBody2);
        assertThat(actual.get(3).getBody()).as("Wrong notification body.").isEqualTo(expectedBody3);
        assertThat(actual.get(1).getNotifiedAt()).as("Wrong notification time.").isEqualTo(expectedTime1);
        assertThat(actual.get(2).getNotifiedAt()).as("Wrong notification time.").isEqualTo(expectedTime2);
        assertThat(actual.get(3).getNotifiedAt()).as("Wrong notification time.").isEqualTo(expectedTime3);
    }
}