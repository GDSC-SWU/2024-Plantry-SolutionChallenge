package com.gdscplantry.plantry.domain.TrackerAndMission.controller;

import com.gdscplantry.plantry.domain.Pantry.domain.*;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission.*;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.tracker.ConsumedProduct;
import com.gdscplantry.plantry.domain.TrackerAndMission.domain.tracker.ConsumedProductRepository;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.model.JwtVo;
import com.gdscplantry.plantry.domain.model.ProductDeleteTypeEnum;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
class TrackerAndMissionControllerTest {
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
    private ConsumedProductRepository consumedProductRepository;

    @Autowired
    private MissionDataRepository missionDataRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private AchievedMissionRepository achievedMissionRepository;

    final String MY_PAGE_API_URL = "/api/v1/mypage";
    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";
    final String PANTRY_TITLE = "My pantry";
    final String[] PRODUCT_NAMES = {"Orange", "Apple", "Melon", "Pasta", "Milk"};
    final BigDecimal[] COUNTS = {BigDecimal.ONE, BigDecimal.valueOf(0.5), BigDecimal.ONE, BigDecimal.valueOf(1.5), BigDecimal.valueOf(2)};
    User user;
    String accessToken;
    Pantry pantry;
    UserPantry userPantry;
    ArrayList<Product> products;

    @BeforeEach
    void addMockData() {
        addMockUser();
        addMockPantry();
        addMockProducts();
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
        pantry = pantryRepository.save(new Pantry(RandomUtil.getUuid(), RandomUtil.getRandomNickname()));
        userPantry = userPantryRepository.save(UserPantry.builder()
                .user(user)
                .pantryId(pantry.getId())
                .title(PANTRY_TITLE)
                .color("A2E5B3")
                .isOwner(true)
                .build());
    }

    void addMockProducts() {
        products = new ArrayList<>();
        for (int i = 0; i < PRODUCT_NAMES.length; i++)
            products.add(Product.builder()
                    .pantryId(pantry.getId())
                    .icon("🍊")
                    .name(PRODUCT_NAMES[i])
                    .storage(StorageEnum.Cold)
                    .count(COUNTS[i])
                    .isUseByDate(true)
                    .date(LocalDate.of(2024, 2, 9))
                    .build());
        products = (ArrayList<Product>) productRepository.saveAll(products);
    }

    void addMockProductDeletion() {
        final int[] TYPES = {1, 2, 3, 4, 1};
        ArrayList<ConsumedProduct> consumedProducts = new ArrayList<>();

        for (int i = 0; i < 5; i++)
            consumedProducts.add(ConsumedProduct.builder()
                    .user(user)
                    .count(COUNTS[i])
                    .type(ProductDeleteTypeEnum.findByKey(TYPES[i]))
                    .product(products.get(i).getName())
                    .foodDataId(products.get(i).getFoodDataId())
                    .addedAt(products.get(i).getCreatedAt())
                    .build());

        consumedProductRepository.saveAll(consumedProducts);
    }

    @Test
    @DisplayName("Get tracker result <200>")
    void getTrackerResult() throws Exception {
        // given
        addMockProductDeletion();
        double[] expected = {50, 8.33, 16.67, 25};

        // when
        ResultActions resultActions = mockMvc.perform(get(MY_PAGE_API_URL + "/track")
                .header("Authorization", "Bearer " + accessToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").exists())
                .andExpect(jsonPath("$.data.result.Ingestion").value(expected[0]))
                .andExpect(jsonPath("$.data.result.Disposal").value(expected[1]))
                .andExpect(jsonPath("$.data.result.Sharing").value(expected[2]))
                .andExpect(jsonPath("$.data.result.Mistake").value(expected[3]))
                .andDo(print());
    }

    @Test
    @DisplayName("Get mission list <200>")
    void getMissionList() throws Exception {
        // given
        List<MissionData> data = missionDataRepository.findAllById(new ArrayList<>(Arrays.asList(1L, 2L, 8L)));
        List<Mission> expected = new ArrayList<>();
        for (MissionData missionData : data) expected.add(new Mission(missionData));
        missionRepository.saveAll(expected);

        // when
        ResultActions resultActions = mockMvc.perform(get(MY_PAGE_API_URL + "/mission")
                .header("Authorization", "Bearer " + accessToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result.length()").value(3))
                .andExpect(jsonPath("$.data.result[0].missionId").value(expected.get(0).getId()))
                .andExpect(jsonPath("$.data.result[1].missionId").value(expected.get(1).getId()))
                .andExpect(jsonPath("$.data.result[2].missionId").value(expected.get(2).getId()))
                .andExpect(jsonPath("$.data.result[2].content").value(data.get(2).getTitle()))
                .andDo(print());
    }

    @Test
    @DisplayName("Update mission achieved <201>")
    void updateMissionAchieved() throws Exception {
        // given
        List<MissionData> data = missionDataRepository.findAllById(new ArrayList<>(Arrays.asList(1L, 2L, 8L)));
        List<Mission> expected = new ArrayList<>();
        for (MissionData missionData : data) expected.add(new Mission(missionData));
        expected = missionRepository.saveAll(expected);

        // when
        ResultActions resultActions = mockMvc.perform(patch(MY_PAGE_API_URL + "/mission")
                .header("Authorization", "Bearer " + accessToken)
                .param("id", String.valueOf(expected.get(0).getId())));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.missionId").value(expected.get(0).getId()))
                .andExpect(jsonPath("$.data.isAchieved").value(true))
                .andDo(print());

        Optional<AchievedMission> achievedMission = achievedMissionRepository.findByMissionAndUser(expected.get(0), user);
        assertThat(achievedMission).as("Mission update failed.").isNotEmpty();
    }
}