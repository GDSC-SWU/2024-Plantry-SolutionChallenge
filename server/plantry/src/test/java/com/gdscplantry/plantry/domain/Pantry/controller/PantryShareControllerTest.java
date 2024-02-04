package com.gdscplantry.plantry.domain.Pantry.controller;

import com.gdscplantry.plantry.domain.Pantry.domain.Pantry;
import com.gdscplantry.plantry.domain.Pantry.domain.PantryRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantry;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.model.JwtVo;
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

    final String PANTRY_SHARE_API_URL = "/api/v1/pantry/share";
    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";
    final String PANTRY_TITLE = "My pantry";
    final String SHARE_CODE = "test123F";
    User user;
    String accessToken;
    Pantry pantry;

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
        userPantryRepository.save(UserPantry.builder()
                .user(user)
                .pantryId(pantry.getId())
                .title(PANTRY_TITLE)
                .color("A2E5B3")
                .isOwner(true)
                .build());
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
}