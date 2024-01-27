package com.gdscplantry.plantry.global.util;

import com.gdscplantry.plantry.domain.Pantry.domain.data.FoodDataRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.data.UseByDateDataRepository;
import com.gdscplantry.plantry.domain.Pantry.vo.FoodDataVo;
import com.gdscplantry.plantry.domain.Pantry.vo.RawFoodDataVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class FoodDataUtil {
    private final FoodDataRepository foodDataRepository;
    private final UseByDateDataRepository useByDateDataRepository;

    public Boolean hasNoUseByDate(ArrayList<RawFoodDataVo> list) {
        for (RawFoodDataVo vo : list)
            if (vo.getDay() != 0)
                return false;

        return true;
    }

    @Transactional(readOnly = true)
    public FoodDataVo findFromFoodDatabase(String query, LocalDate current) {
        /*
         * FoodData O / Use-By-Date O -> single / multiple
         * FoodData O / Use-By-Date X -> emoji only
         * FoodData X / Use-By-Date O -> use-by-date only
         * FoodData X / Use-By-Date X -> none
         */

        boolean isFromFoodDb = true;
        Long foodDataId = null;
        String emoji = null;
        Integer day;

        // Find from food database
        ArrayList<RawFoodDataVo> list = foodDataRepository.findAllByNameContainsQueryWithJPQL(query);

        // Find from use-by-date database (if not in food database)
        if (list.size() == 0 || hasNoUseByDate(list)) {
            isFromFoodDb = false;
            list = useByDateDataRepository.findAllByNameContainsQueryWithJPQL(query);
        }

        // If there is no data
        if (list.size() == 0)
            return null;

        // Find foodDataId & default emoji
        if (isFromFoodDb) {
            foodDataId = list.get(0).getFoodDataId();
            emoji = list.get(0).getEmoji();
        }

        // Find use-by-date data
        double average = list
                .stream()
                .mapToDouble(RawFoodDataVo::getDay)
                .average().orElse(0);
        day = average == 0 ? null : (int) average;

        return FoodDataVo.builder()
                .foodDataId(foodDataId)
                .emoji(emoji)
                .value(day)
                .current(current)
                .build();
    }
}
