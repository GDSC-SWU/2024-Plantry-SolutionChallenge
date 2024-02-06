package com.gdscplantry.plantry.domain.Pantry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdscplantry.plantry.domain.Pantry.domain.*;
import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductListReqDto;
import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductReqDto;
import com.gdscplantry.plantry.domain.Pantry.dto.product.UpdateProductReqDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
class ProductControllerTest {
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

    final String PANTRY_API_URL = "/api/v1/pantry/product";
    final String EMAIL = "test@test.com";
    final String NICKNAME = "test_name";
    final String COLOR = "A2E5B3";
    final String[] names = {"peach", "apple", "orange juice"};

    User user;
    Long[] pantries = new Long[3];
    String accessToken;

    @BeforeEach
    void addMockData() {
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

        // Save user pantries
        addMockPantries();
    }

    void addMockPantries() {
        String[] titles = {"pantry1", "pantry2", "pantry3"};

        for (int i = 0; i < 3; i++) {
            Long pantry = pantryRepository.save(new Pantry(RandomUtil.getUuid(), RandomUtil.getRandomNickname())).getId();
            pantries[i] = pantry;
            userPantryRepository.save(UserPantry.builder()
                    .user(user)
                    .pantryId(pantry)
                    .title(titles[i])
                    .color(COLOR)
                    .build());
        }
    }

    @AfterEach
    void removeRedisData() {
        // Remove refreshToken from redis
        redisUtil.delete(user.getId() + "_refresh");
    }

    @Test
    @DisplayName("Add single product <201>")
    void addSingleProduct() throws Exception {
        // given
        NewProductReqDto dto = new NewProductReqDto(pantries[0], "🍑", names[0], false, "2024-01-30", "Cold", String.valueOf(2));

        // when
        ResultActions resultActions = mockMvc.perform(post(PANTRY_API_URL + "/single")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.pantryId").exists())
                .andExpect(jsonPath("$.data.name").value(names[0]))
                .andExpect(jsonPath("$.data.isNotified").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("Add multiple products <201>")
    void addMultipleProducts() throws Exception {
        ArrayList<NewProductReqDto> reqDtos = new ArrayList<>();
        // given
        for (int i = 0; i < 3; i++)
            reqDtos.add(new NewProductReqDto(pantries[i], "🍑", names[i], false, "2024-01-30", "Cold", String.valueOf(2)));
        NewProductListReqDto newProductListReqDto = new NewProductListReqDto(reqDtos);

        // when
        ResultActions resultActions = mockMvc.perform(post(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newProductListReqDto)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.result[0].pantryId").value(pantries[0]))
                .andExpect(jsonPath("$.data.result[0].name").value(names[0]))
                .andExpect(jsonPath("$.data.result[2].name").value(names[2]))
                .andDo(print());
    }

    @Test
    @DisplayName("Update product <201>")
    void updateProduct() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 2, 1);
        Long productId = productRepository.save(Product.builder()
                .pantryId(pantries[0])
                .icon("🥕")
                .name(names[0])
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(true)
                .date(date)
                .build()
        ).getId();
        UpdateProductReqDto dto = new UpdateProductReqDto("🍑", names[0], true, date.format(DateTimeFormatter.ISO_DATE), "Freeze", String.valueOf(1));

        // when
        ResultActions resultActions = mockMvc.perform(patch(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .param("product", productId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.pantryId").value(pantries[0]))
                .andExpect(jsonPath("$.data.icon").value("🍑"))
                .andExpect(jsonPath("$.data.name").value(names[0]))
                .andExpect(jsonPath("$.data.storage").value(StorageEnum.Freeze.getKey()))
                .andDo(print());
    }

    @Test
    @DisplayName("Update product count <201>")
    void updateProductCount() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 2, 1);
        Long productId = productRepository.save(Product.builder()
                .pantryId(pantries[0])
                .icon("🥕")
                .name(names[0])
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(true)
                .date(date)
                .build()
        ).getId();

        // when
        ResultActions resultActions = mockMvc.perform(patch(PANTRY_API_URL + "/count")
                .header("Authorization", "Bearer " + accessToken)
                .param("product", productId.toString())
                .param("count", String.valueOf(0.5)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.pantryId").value(pantries[0]))
                .andExpect(jsonPath("$.data.name").value(names[0]))
                .andExpect(jsonPath("$.data.count").value(0.5))
                .andDo(print());
    }

    @Test
    @DisplayName("Delete product count <200>")
    void deleteProduct() throws Exception {
        // given
        Long productId = productRepository.save(Product.builder()
                .pantryId(pantries[0])
                .icon("🥕")
                .name(names[0])
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(true)
                .date(LocalDate.of(2024, 2, 1))
                .build()
        ).getId();

        // when
        ResultActions resultActions = mockMvc.perform(delete(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .param("product", productId.toString())
                .param("type", String.valueOf(1))
                .param("count", String.valueOf(0.5)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(productId))
                .andExpect(jsonPath("$.data.type").value(ProductDeleteTypeEnum.Ingestion.getTitle()))
                .andExpect(jsonPath("$.data.result").value(0.5))
                .andDo(print());

        ArrayList<ConsumedProduct> consumedProducts = consumedProductRepository.findAllByUser(user);
        assertThat(consumedProducts.get(0).getProduct()).as("Product consumption data save test failed.").isEqualTo(names[0]);
    }

    @Test
    @DisplayName("Delete product count <200> 2")
    void deleteProduct2() throws Exception {
        // given
        Long productId = productRepository.save(Product.builder()
                .pantryId(pantries[0])
                .icon("🥕")
                .name(names[0])
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(true)
                .date(LocalDate.of(2024, 2, 1))
                .build()
        ).getId();

        // when
        ResultActions resultActions = mockMvc.perform(delete(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .param("product", productId.toString())
                .param("type", String.valueOf(1))
                .param("count", String.valueOf(1)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(productId))
                .andExpect(jsonPath("$.data.type").value(ProductDeleteTypeEnum.Ingestion.getTitle()))
                .andExpect(jsonPath("$.data.result").value(0))
                .andDo(print());

        ArrayList<ConsumedProduct> consumedProducts = consumedProductRepository.findAllByUser(user);
        assertThat(consumedProducts.get(0).getProduct()).as("Product consumption data save test failed.").isEqualTo(names[0]);
    }

    @Test
    @DisplayName("Delete product count <400>")
    void deleteProduct_fail400() throws Exception {
        // given
        Long productId = productRepository.save(Product.builder()
                .pantryId(pantries[0])
                .icon("🥕")
                .name(names[0])
                .storage(StorageEnum.Cold)
                .count(BigDecimal.ONE)
                .isUseByDate(true)
                .date(LocalDate.of(2024, 2, 1))
                .build()
        ).getId();

        // when
        ResultActions resultActions = mockMvc.perform(delete(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .param("product", productId.toString())
                .param("type", String.valueOf(1))
                .param("count", String.valueOf(2)));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(print());

        ArrayList<ConsumedProduct> consumedProducts = consumedProductRepository.findAllByUser(user);
        assertThat(consumedProducts.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Read product list <200>")
    void readProductList() throws Exception {
        // given
        for (int i = 0; i < 3; i++)
            productRepository.save(new NewProductReqDto(pantries[0], "🍑", names[i], false, "2024-01-30", "Cold", String.valueOf(2)).toEntity());
        for (int i = 0; i < 3; i++)
            productRepository.save(new NewProductReqDto(pantries[0], "🍑", names[i], false, "2024-01-24", "Cold", String.valueOf(2)).toEntity());
        for (int i = 0; i < 3; i++)
            productRepository.save(new NewProductReqDto(pantries[0], "🍑", names[i], false, "2024-02-15", "Cold", String.valueOf(2)).toEntity());

        // when
        ResultActions resultActions = mockMvc.perform(get(PANTRY_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .param("pantry", pantries[0].toString())
                .param("filter", "Cold")
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.filter").value("Cold"))
                .andExpect(jsonPath("$.data.result['-1'].length()").value(6))
                .andDo(print());
    }


    @Test
    @DisplayName("Search product list <200>")
    void searchProductList() throws Exception {
        // given
        for (int i = 0; i < 3; i++)
            productRepository.save(new NewProductReqDto(pantries[0], "🍑", names[i], false, "2024-01-30", "Cold", String.valueOf(2)).toEntity());
        for (int i = 0; i < 3; i++)
            productRepository.save(new NewProductReqDto(pantries[0], "🍑", names[i], false, "2024-01-24", "Freeze", String.valueOf(2)).toEntity());
        for (int i = 0; i < 3; i++)
            productRepository.save(new NewProductReqDto(pantries[0], "🍑", names[i], false, "2024-02-15", "Cold", String.valueOf(2)).toEntity());
        String query = "p";

        // when
        ResultActions resultActions = mockMvc.perform(get(PANTRY_API_URL + "/query")
                .header("Authorization", "Bearer " + accessToken)
                .param("pantry", pantries[0].toString())
                .param("filter", "Cold")
                .param("query", query)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.filter").value("Cold"))
                .andExpect(jsonPath("$.data.result['4'].length()").value(2))
                .andDo(print());
    }
}