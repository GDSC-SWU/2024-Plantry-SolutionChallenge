package com.gdscplantry.plantry.domain.TrackerAndMission.service;

import com.gdscplantry.plantry.domain.TrackerAndMission.domain.tracker.ConsumedProductRepository;
import com.gdscplantry.plantry.domain.TrackerAndMission.vo.ConsumptionDataVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.model.ProductDeleteTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackerService {
    private final ConsumedProductRepository consumedProductRepository;

    private final String[] TYPES = {ProductDeleteTypeEnum.Ingestion.getTitle(), ProductDeleteTypeEnum.Disposal.getTitle(),
            ProductDeleteTypeEnum.Sharing.getTitle(), ProductDeleteTypeEnum.Mistake.getTitle()};

    @Transactional(readOnly = true)
    public Map<String, Double> getTrackerResult(User user) {
        // Find monday's date on this week
        LocalDateTime today = LocalDateTime.now().with(LocalTime.of(0, 0));
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        LocalDateTime monday = dayOfWeek == DayOfWeek.MONDAY ? today : today.minusDays(dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue());

        // Find data
        ArrayList<ConsumptionDataVo> data = consumedProductRepository.findAllByUserAndCreatedAtWithJPQL(user, monday);
        Map<String, Double> result = new LinkedHashMap<>();
        double sum = 0;
        for (ConsumptionDataVo vo : data) {
            double value = vo.getValue() == null ? 0 : vo.getValue().doubleValue();
            sum += value;
            result.put(vo.getType().getTitle(), value);
        }

        // Calculate portion
        for (String type : TYPES) {
            double value = result.get(type) == null ? 0. : result.get(type);
            if (sum == 0) result.put(type, 0.);
            else result.replace(type, Math.round((value / sum) * 10000) / 100.0);
        }

        return result;
    }
}
