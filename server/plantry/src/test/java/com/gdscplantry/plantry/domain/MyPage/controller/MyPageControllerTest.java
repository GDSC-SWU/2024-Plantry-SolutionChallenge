package com.gdscplantry.plantry.domain.MyPage.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class MyPageControllerTest {
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

    final String MY_PAGE_API_URL = "/api/v1/mypage";
    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";
    final String PANTRY_TITLE = "My pantry";
    User user;
    String accessToken;
    Product product1;
    Product product2;

    @AfterEach
    void removeRedisData() {
        // Remove refreshToken from redis
        redisUtil.delete(user.getId() + "_refresh");
    }

    @BeforeEach
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
        Pantry pantry = pantryRepository.save(new Pantry(RandomUtil.getUuid(), RandomUtil.getRandomNickname()));
        userPantryRepository.save(UserPantry.builder()
                .user(user)
                .pantryId(pantry.getId())
                .title(PANTRY_TITLE)
                .color("A2E5B3")
                .build());

        // Save products
        String productName1 = "Orange";
        String productName2 = "Apple";

        product1 = productRepository.save(Product.builder()
                .pantryId(pantry.getId())
                .icon("🍊")
                .name(productName1)
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(true)
                .date(LocalDate.of(2024, 2, 10))
                .build());

        product2 = productRepository.save(Product.builder()
                .pantryId(pantry.getId())
                .icon("🍎")
                .name(productName2)
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(false)
                .date(LocalDate.of(2024, 2, 5))
                .build());
        product2.updateFoodData(FoodDataVo.builder()
                .value(10)
                .current(LocalDate.now())
                .foodDataId(1L)
                .build());
    }

    void addMockNotifications() {
        int[] typeKeys = {0, 1, 3, 7, 10, 11, 13, 17};
        ArrayList<Notification> notifications = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            LocalDateTime notifiedAt = LocalDateTime.of(product1.getDate().minusDays(typeKeys[i]), LocalTime.of(9, 0));
            notifications.add(Notification.builder()
                    .user(user)
                    .typeKey(typeKeys[i])
                    .title(NotificationTypeEnum.getTitleStr(product1.getName(), typeKeys[i]))
                    .body(NotificationTypeEnum.getBodyStr(PANTRY_TITLE, product1.getName(), typeKeys[i], product1.getDate(), null))
                    .entityId(product1.getId())
                    .notifiedAt(notifiedAt)
                    .isDeleted(notifiedAt.isBefore(LocalDateTime.now()))
                    .build());
        }

        for (int i = 0; i < 4; i++) {
            LocalDateTime notifiedAt = LocalDateTime.of(product2.getDate().minusDays(typeKeys[i]), LocalTime.of(9, 0));
            notifications.add(Notification.builder()
                    .user(user)
                    .typeKey(typeKeys[i + 4])
                    .title(NotificationTypeEnum.getTitleStr(product2.getName(), typeKeys[i + 4]))
                    .body(NotificationTypeEnum.getBodyStr(PANTRY_TITLE, product2.getName(), typeKeys[i + 4], product2.getDate(), product2.getUseByDateData()))
                    .entityId(product2.getId())
                    .notifiedAt(notifiedAt)
                    .isDeleted(notifiedAt.isBefore(LocalDateTime.now()))
                    .build());
        }

        notificationRepository.saveAll(notifications);
    }

    @Test
    @DisplayName("Get user profile <200>")
    void getUserProfile() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get(MY_PAGE_API_URL + "/user")
                .header("Authorization", "Bearer " + accessToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.nickname").value(NICKNAME))
                .andExpect(jsonPath("$.data.profileImagePath").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("Update nickname <201>")
    void updateNickname() throws Exception {
        // given
        String newNickname = "new";

        // when
        ResultActions resultActions = mockMvc.perform(patch(MY_PAGE_API_URL + "/nickname")
                .header("Authorization", "Bearer " + accessToken)
                .param("nickname", newNickname));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nickname").value(newNickname))
                .andDo(print());

        String actual = userRepository.findById(user.getId())
                .orElseThrow(() -> new Exception("null"))
                .getNickname();
        assertThat(actual).as("Nickname update failed.").isEqualTo(newNickname);
    }

    @Test
    @DisplayName("Update nickname <400>")
    void updateNickname_400_already_exists() throws Exception {
        // given
        String newNickname = "new";
        userRepository.save(User.builder()
                .email("test2@test.com")
                .nickname(newNickname)
                .build());

        // when
        ResultActions resultActions = mockMvc.perform(patch(MY_PAGE_API_URL + "/nickname")
                .header("Authorization", "Bearer " + accessToken)
                .param("nickname", newNickname));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Nickname already exists."))
                .andDo(print());

        String actual = userRepository.findById(user.getId())
                .orElseThrow(() -> new Exception("null"))
                .getNickname();
        assertThat(actual).as("Nickname exception handling failed.").isEqualTo(NICKNAME);
    }

    @Test
    @DisplayName("Update notification time <201>")
    void updateNotificationTime() throws Exception {
        // given
        addMockProduct();
        addMockNotifications();
        int newTime = 20;

        // when
        ResultActions resultActions = mockMvc.perform(patch(MY_PAGE_API_URL + "/notif")
                .header("Authorization", "Bearer " + accessToken)
                .param("time", String.valueOf(newTime)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.time").value(newTime))
                .andDo(print());

        ArrayList<Notification> actual = notificationRepository.findAllByEntityIdAndUserAndTypeKeyLessThan(product1.getId(), user, 20);
        actual.addAll(notificationRepository.findAllByEntityIdAndUserAndTypeKeyLessThan(product2.getId(), user, 20));
        for (Notification notification : actual)
            if (!notification.getIsDeleted())
                assertThat(notification.getNotifiedAt().getHour()).as("NotifiedAt update failed.").isEqualTo(newTime);
    }

    @Test
    @DisplayName("Get terms <200>")
    void getTerms() throws Exception {
        // given
        String titleUse = "Terms of Use";
        String titlePrivacy = "Privacy Policy";
        String expectedContentUse = "test - terms of use";
        String expectedContentPrivacy = "test - privacy policy";
        String type = "all";

        // when
        ResultActions resultActions = mockMvc.perform(get(MY_PAGE_API_URL + "/terms")
                .param("type", type));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value(type))
                .andExpect(jsonPath("$.data.result.length()").value(2))
                .andExpect(jsonPath("$.data.result[0].title").value(titleUse))
                .andExpect(jsonPath("$.data.result[0].content").value(expectedContentUse))
                .andExpect(jsonPath("$.data.result[1].title").value(titlePrivacy))
                .andExpect(jsonPath("$.data.result[1].content").value(expectedContentPrivacy))
                .andDo(print());
    }
}