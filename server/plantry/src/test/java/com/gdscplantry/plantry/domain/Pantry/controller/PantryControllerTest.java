package com.gdscplantry.plantry.domain.Pantry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscplantry.plantry.domain.Pantry.domain.Pantry;
import com.gdscplantry.plantry.domain.Pantry.domain.PantryRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantry;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.dto.pantry.NewPantryReqDto;
import com.gdscplantry.plantry.domain.Pantry.dto.pantry.UpdatePantryReqDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private PantryRepository pantryRepository;

    @Autowired
    private UserPantryRepository userPantryRepository;

    final String PANTRY_API_URL = "/api/v1/pantry";
    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";
    final String COLOR = "A2E5B3";

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
    @DisplayName("Read pantry list")
    void readPantryList_200() throws Exception {
        // given
        String[] titles = {"pantry1", "pantry2", "pantry3"};
        Long[] pantries = new Long[3];

        for (int i = 0; i < 3; i++) {
            pantries[i] = pantryRepository.save(new Pantry(RandomUtil.getUuid())).getId();
            userPantryRepository.save(
                    UserPantry.builder()
                            .user(user)
                            .pantryId(pantries[i])
                            .title(titles[i])
                            .color(COLOR)
                            .build()
            );
        }

        // when
        ResultActions resultActions = mockMvc.perform(get(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").exists())
                .andExpect(jsonPath("$.data.result[0].id").value(pantries[0]))
                .andExpect(jsonPath("$.data.result[0].title").value(titles[0]))
                .andExpect(jsonPath("$.data.result[0].color").value(COLOR))
                .andExpect(jsonPath("$.data.result[0].isMarked").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("Add new pantry <201>")
    void addNewPantry_201() throws Exception {
        // given
        String title = "new_pantry";
        NewPantryReqDto newPantryReqDto = new NewPantryReqDto(title, COLOR);

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
                .andExpect(jsonPath("$.data.color").value(COLOR))
                .andDo(print());
    }

    @Test
    @DisplayName("Update pantry <201>")
    void updatePantry_201() throws Exception {
        // given
        String title = "new_pantry";
        String updatedTitle = "old_pantry";
        Long pantryId = pantryRepository.save(new Pantry(RandomUtil.getUuid())).getId();
        userPantryRepository.save(
                UserPantry.builder()
                        .user(user)
                        .pantryId(pantryId)
                        .title(title)
                        .color(COLOR)
                        .build()
        );
        UpdatePantryReqDto updatePantryReqDto = new UpdatePantryReqDto(updatedTitle, COLOR);

        // when
        ResultActions resultActions = mockMvc.perform(patch(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .param("id", String.valueOf(pantryId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatePantryReqDto)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.pantryId").exists())
                .andExpect(jsonPath("$.data.title").value(updatedTitle))
                .andExpect(jsonPath("$.data.color").value(COLOR))
                .andDo(print());

        UserPantry foundPantry = userPantryRepository.findByPantryIdAndUser(pantryId, user)
                .orElseThrow(() -> new Exception("null"));

        assertThat(foundPantry.getTitle()).as("Data update failed.").isEqualTo(updatedTitle);
        assertThat(foundPantry.getUpdatedAt()).as("Data update failed.").isNotNull();
    }

    @Test
    @DisplayName("Delete pantry <200>")
    void deletePantry_200() throws Exception {
        // given
        String title = "new_pantry";
        Long pantryId = pantryRepository.save(new Pantry(RandomUtil.getUuid())).getId();
        userPantryRepository.save(
                UserPantry.builder()
                        .user(user)
                        .pantryId(pantryId)
                        .title(title)
                        .color(COLOR)
                        .build()
        );

        // when
        ResultActions resultActions = mockMvc.perform(delete(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .param("id", String.valueOf(pantryId)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(userPantryRepository.findByPantryIdAndUser(pantryId, user)).as("Data deletion failed.").isEmpty();
        assertThat(pantryRepository.findById(pantryId)).as("Data deletion failed.").isEmpty();
    }

    @Test
    @DisplayName("Set pantry marked <201>")
    void setPantryMarked_201() throws Exception {
        // given
        String title = "new_pantry";
        Long pantryId = pantryRepository.save(new Pantry(RandomUtil.getUuid())).getId();
        userPantryRepository.save(
                UserPantry.builder()
                        .user(user)
                        .pantryId(pantryId)
                        .title(title)
                        .color(COLOR)
                        .build()
        );

        // when
        ResultActions resultActions = mockMvc.perform(patch(PANTRY_API_URL + "/mark")
                .header("Authorization", "Bearer " + accessToken)
                .param("id", String.valueOf(pantryId)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(pantryId))
                .andExpect(jsonPath("$.data.isMarked").value(true))
                .andDo(print());

        UserPantry foundPantry = userPantryRepository.findByPantryIdAndUser(pantryId, user)
                .orElseThrow(() -> new Exception("null"));

        assertThat(foundPantry.getIsMarked()).as("Data update failed.").isTrue();
        assertThat(foundPantry.getUpdatedAt()).as("Data update failed.").isNotNull();
    }
}