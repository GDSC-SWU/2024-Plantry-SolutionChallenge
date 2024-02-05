package com.gdscplantry.plantry.domain.Pantry.controller;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Notification.domain.NotificationRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class PantryShareControllerTest {
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

    private final int[] TYPE_KEYS = {0, 1, 3, 7, 10, 11, 13, 17};
    final String PANTRY_SHARE_API_URL = "/api/v1/pantry/share";
    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";
    final String PANTRY_TITLE = "My pantry";
    final String PANTRY_COLOR = "A2E5B3";
    final String PRODUCT_NAME1 = "Orange";
    final String PRODUCT_NAME2 = "Apple";
    final String SHARE_CODE = "test123F";
    User user;
    String accessToken;
    Pantry pantry;
    UserPantry userPantry;
    Product product1;
    Product product2;

    @BeforeEach
    void addMockData() {
        addMockUser();
        addMockPantry();
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

    void addMockPantry() {
        pantry = pantryRepository.save(new Pantry(RandomUtil.getUuid(), SHARE_CODE));
        userPantry = userPantryRepository.save(UserPantry.builder()
                .user(user)
                .pantryId(pantry.getId())
                .title(PANTRY_TITLE)
                .color(PANTRY_COLOR)
                .isOwner(true)
                .build());
    }

    void addMockProduct() {
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
        ArrayList<Notification> notifications = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            LocalDateTime notifiedAt = LocalDateTime.of(product1.getDate().minusDays(TYPE_KEYS[i]), LocalTime.of(9, 0));
            notifications.add(Notification.builder()
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
            notifications.add(Notification.builder()
                    .user(user)
                    .typeKey(TYPE_KEYS[i + 4])
                    .title(NotificationTypeEnum.getTitleStr(product2.getName(), TYPE_KEYS[i + 4]))
                    .body(NotificationTypeEnum.getBodyStr(userPantry.getTitle(), product2.getName(), TYPE_KEYS[i + 4], product2.getDate(), product2.getUseByDateData()))
                    .entityId(product2.getId())
                    .notifiedAt(notifiedAt)
                    .build());
        }

        notificationRepository.saveAll(notifications);
    }

    @Test
    @DisplayName("Get share-code <200>")
    void getCode() throws Exception {
        // given
        Long pantryId = pantry.getId();

        // when
        ResultActions resultActions = mockMvc.perform(get(PANTRY_SHARE_API_URL + "/code")
                .header("Authorization", "Bearer " + accessToken)
                .param("pantry", String.valueOf(pantryId)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pantryId").value(pantryId))
                .andExpect(jsonPath("$.data.code").value(SHARE_CODE))
                .andDo(print());
    }

    @Test
    @DisplayName("Refresh share-code <201>")
    void refreshCode() throws Exception {
        // given
        Long pantryId = pantry.getId();

        // when
        ResultActions resultActions = mockMvc.perform(patch(PANTRY_SHARE_API_URL + "/code")
                .header("Authorization", "Bearer " + accessToken)
                .param("pantry", String.valueOf(pantryId)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.pantryId").value(pantryId))
                .andExpect(jsonPath("$.data.code").exists())
                .andDo(print());

        Pantry actual = pantryRepository.findById(pantryId)
                .orElseThrow(() -> new Exception("null"));
        assertThat(actual.getCode()).as("Code update failed.").isNotEqualTo(SHARE_CODE);
    }

    @Test
    @DisplayName("Refresh share-code <403>")
    void refreshCode_403() throws Exception {
        // given
        Pantry newPantry = pantryRepository.save(new Pantry(RandomUtil.getUuid(), RandomUtil.getRandomNickname()));
        userPantryRepository.save(UserPantry.builder()
                .user(user)
                .pantryId(newPantry.getId())
                .title(PANTRY_TITLE)
                .color("A2E5B3")
                .isOwner(false)
                .build());
        Long pantryId = newPantry.getId();

        // when
        ResultActions resultActions = mockMvc.perform(patch(PANTRY_SHARE_API_URL + "/code")
                .header("Authorization", "Bearer " + accessToken)
                .param("pantry", String.valueOf(pantryId)));

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("Post share-code <201>")
    void postCode() throws Exception {
        // given
        addMockProduct();
        addMockNotifications();
        Long pantryId = pantry.getId();
        User newUser = userRepository.save(User.builder()
                .email("newUser@test.com")
                .nickname("newUser")
                .build());
        JwtVo vo = jwtUtil.generateTokens(newUser);
        String newUserAccessToken = vo.getAccessToken();
        redisUtil.opsForValueSet(newUser.getId() + "_refresh", vo.getRefreshToken(), 24 * 7);

        // when
        ResultActions resultActions = mockMvc.perform(post(PANTRY_SHARE_API_URL + "/code")
                .header("Authorization", "Bearer " + newUserAccessToken)
                .param("code", SHARE_CODE));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.pantryId").value(pantryId))
                .andExpect(jsonPath("$.data.title").value(PANTRY_TITLE))
                .andExpect(jsonPath("$.data.color").value(PANTRY_COLOR))
                .andDo(print());

        ArrayList<Notification> actual = notificationRepository.findAllByUser(newUser);
        assertThat(actual.size()).as("Notification save failed.").isEqualTo(8);
        assertThat(actual.get(0).getEntityId()).as("Notification save failed.").isEqualTo(product1.getId());

        redisUtil.delete(newUser.getId() + "_refresh");
    }
}