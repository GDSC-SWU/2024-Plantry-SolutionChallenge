package com.gdscplantry.plantry.global.util;

import com.gdscplantry.plantry.domain.Pantry.vo.FoodDataVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@Transactional
class FoodDataUtilTest {
    @Autowired
    private FoodDataUtil foodDataUtil;

    @Test
    @DisplayName("Find from food database")
    void findFromFoodDatabase() {
        // given
        String query = "cheese";
        LocalDate current = LocalDate.now();

        // when
        FoodDataVo vo = foodDataUtil.findFromFoodDatabase(query, current);

        // then
        assertThat(vo.getUseByDateData()).as("Data search failed").isNotNull();
        log.info(vo.getFoodDataId() + "/" + vo.getEmoji() + "/" + vo.getUseByDateData());
    }
}