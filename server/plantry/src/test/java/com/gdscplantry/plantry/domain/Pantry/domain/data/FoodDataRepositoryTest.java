package com.gdscplantry.plantry.domain.Pantry.domain.data;

import com.gdscplantry.plantry.domain.Pantry.vo.RawFoodDataVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class FoodDataRepositoryTest {
    @Autowired
    private FoodDataRepository foodDataRepository;

    @Test
    @DisplayName("Find all by name")
    void findAllByNameWithJPQL() {
        // given
        String query = "apple";

        // when
        ArrayList<RawFoodDataVo> list = foodDataRepository.findAllByNameContainsQueryWithJPQL(query);

        // then
        assertThat(list.size()).as("Search failed").isNotZero();
        for (RawFoodDataVo vo : list)
            log.info(vo.getEmoji() + " / " + vo.getFoodDataId() + " / " + vo.getDay());
    }
}