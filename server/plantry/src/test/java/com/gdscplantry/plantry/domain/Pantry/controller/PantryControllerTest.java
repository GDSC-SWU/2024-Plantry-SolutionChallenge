package com.gdscplantry.plantry.domain.Pantry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscplantry.plantry.domain.Pantry.dto.NewPantryReqDto;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.model.JwtVo;
import com.gdscplantry.plantry.global.util.JwtUtil;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class PantryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserRepository userRepository;

    final String PANTRY_API_URL = "/api/v1/pantry";
    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";

    User user;
    String accessToken;

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

    @AfterEach
    void removeRedisData() {
        // Remove refreshToken from redis
        redisUtil.delete(user.getId() + "_refresh");
    }

    @Test
    @DisplayName("Add new pantry successfully")
    void addNewPantry_Suc() throws Exception {
        // given
        String title = "new_pantry";
        String color = "A2E5B3";
        NewPantryReqDto newPantryReqDto = new NewPantryReqDto(title, color);

        // when
        ResultActions resultActions = mockMvc.perform(post(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newPantryReqDto)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.pantryId").exists())
                .andExpect(jsonPath("$.data.title").value(title))
                .andExpect(jsonPath("$.data.color").value(color))
                .andDo(print());
    }

}