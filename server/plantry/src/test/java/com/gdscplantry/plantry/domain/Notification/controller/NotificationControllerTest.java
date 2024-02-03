package com.gdscplantry.plantry.domain.Notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Notification.domain.NotificationRepository;
import com.gdscplantry.plantry.domain.Notification.dto.UpdateProductNotificationReqDto;
import com.gdscplantry.plantry.domain.Pantry.domain.*;
import com.gdscplantry.plantry.domain.Pantry.vo.FoodDataVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.model.JwtVo;
import com.gdscplantry.plantry.domain.model.NotificationTypeEnum;
import com.gdscplantry.plantry.domain.model.StorageEnum;
import com.gdscplantry.plantry.global.util.JwtUtil;
import com.gdscplantry.plantry.global.util.RandomUtil;
import com.gdscplantry.plantry.global.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

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

    final String NOTIFICATION_API_URL = "/api/v1/notif";
    private final int[] TYPE_KEYS = {0, 1, 3, 7, 10, 11, 13, 17};
    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";
    final String COLOR = "A2E5B3";
    final String PANTRY_TITLE = "My pantry";
    final String PRODUCT_NAME1 = "Orange";
    final String PRODUCT_NAME2 = "Apple";
    User user;
    String accessToken;
    Pantry pantry;
    UserPantry userPantry;
    Product product1;
    Product product2;
    ArrayList<Notification> notifications1 = new ArrayList<>();
    ArrayList<Notification> notifications2 = new ArrayList<>();

    @BeforeEach
    void addMockData() {
        addMockUser();
        addMockProduct();
        addMockNotifications();
    }

    @AfterEach
    void removeRedisData() {
        // Remove refreshToken from redis
        redisUtil.delete(user.getId() + "_refresh");
    }

    void addMockUser() {
        // Save user
        user = userRepository.save(User.builder()
                .email(EMAIL)
                .nickname(NICKNAME)
                .build());

        // Generate JWT
        JwtVo jwtVo = jwtUtil.generateTokens(user);

        // Get access token
        accessToken = jwtVo.getAccessToken();

        // Save refreshToken to redis
        redisUtil.opsForValueSet(user.getId() + "_refresh", jwtVo.getRefreshToken(), 24 * 7);
    }

    void addMockProduct() {
        // Save pantry
        pantry = pantryRepository.save(new Pantry(RandomUtil.getUuid(), RandomUtil.getRandomNickname()));
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
                .date(LocalDate.of(2024, 1, 30))
                .build());

        product2 = productRepository.save(Product.builder()
                .pantryId(pantry.getId())
                .icon("🍎")
                .name(PRODUCT_NAME2)
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(false)
                .date(LocalDate.of(2024, 1, 31))
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
    @DisplayName("Read product's notification list <200>")
    void readProductNotificationList_200() throws Exception {
        // given
        Long productId = product1.getId();
        notificationRepository.delete(notifications1.get(3));

        // when
        ResultActions resultActions = mockMvc.perform(get(NOTIFICATION_API_URL + "/product")
                .header("Authorization", "Bearer " + accessToken)
                .param("product", productId.toString()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").exists())
                .andExpect(jsonPath("$.data.result.length()").value(3))
                .andExpect(jsonPath("$.data.result[0]").value(0))
                .andExpect(jsonPath("$.data.result[1]").value(1))
                .andExpect(jsonPath("$.data.result[2]").value(3))
                .andDo(print());
    }

    @Test
    @DisplayName("Update product's notification list <201>")
    void updateProductNotificationList_201() throws Exception {
        // given
        Long productId = product1.getId();
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(0, 1, 7));
        UpdateProductNotificationReqDto dto = new UpdateProductNotificationReqDto(expected);

        // when
        ResultActions resultActions = mockMvc.perform(patch(NOTIFICATION_API_URL + "/product")
                .header("Authorization", "Bearer " + accessToken)
                .param("product", productId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.result").exists())
                .andExpect(jsonPath("$.data.result.length()").value(3))
                .andExpect(jsonPath("$.data.result[0]").value(0))
                .andExpect(jsonPath("$.data.result[1]").value(1))
                .andExpect(jsonPath("$.data.result[2]").value(7))
                .andDo(print());

        ArrayList<Integer> actual = notificationRepository.findAllTypeByUserAndEntityIdAndTypeKeyWithJPQL(user, productId);
        assertThat(actual.size()).as("Notification update failed.").isEqualTo(3);
        assertThat(actual.get(2)).as("Notification update failed.").isEqualTo(7);
    }

    @Test
    @DisplayName("Read user's notification list <200>")
    void readNotificationList_200() throws Exception {
        // given
        notificationRepository.delete(notifications1.get(3));

        // when
        ResultActions resultActions = mockMvc.perform(get(NOTIFICATION_API_URL)
                .header("Authorization", "Bearer " + accessToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").exists())
                .andExpect(jsonPath("$.data.result.length()").value(7))
                .andExpect(jsonPath("$.data.result[2].id").value(notifications2.get(1).getId()))
                .andExpect(jsonPath("$.data.result[2].type").value("Exp"))
                .andExpect(jsonPath("$.data.result[2].title").value(notifications2.get(1).getTitle()))
                .andExpect(jsonPath("$.data.result[2].body").value(notifications2.get(1).getBody()))
                .andExpect(jsonPath("$.data.result[2].notifiedAt").value(notifications2.get(1).getNotifiedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.data.result[2].isChecked").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("Set notification checked <200>")
    void checkNotification_200() throws Exception {
        // given
        Long notificationId = notifications1.get(3).getId();

        // when
        ResultActions resultActions = mockMvc.perform(patch(NOTIFICATION_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .param("id", notificationId.toString()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(notificationId))
                .andDo(print());

        Notification actual = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new Exception("Null"));
        assertThat(actual.getIsChecked()).as("Data update failed").isTrue();
    }
}