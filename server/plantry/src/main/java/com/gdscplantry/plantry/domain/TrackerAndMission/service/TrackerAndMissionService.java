package com.gdscplantry.plantry.domain.TrackerAndMission.service;

import com.gdscplantry.plantry.domain.TrackerAndMission.domain.tracker.ConsumedProductRepository;
import com.gdscplantry.plantry.domain.TrackerAndMission.dto.TrackerResDto;
import com.gdscplantry.plantry.domain.TrackerAndMission.vo.ConsumptionDataVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.model.ProductDeleteTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackerAndMissionService {
    private final ConsumedProductRepository consumedProductRepository;

    private final String[] TYPES = {ProductDeleteTypeEnum.Ingestion.getTitle(), ProductDeleteTypeEnum.Disposal.getTitle(),
            ProductDeleteTypeEnum.Sharing.getTitle(), ProductDeleteTypeEnum.Mistake.getTitle()};

    @Transactional(readOnly = true)
    public TrackerResDto getTrackerResult(User user) {
        // Find monday's date on this week
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        LocalDateTime monday = dayOfWeek == DayOfWeek.MONDAY ? now : now.minusDays(dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue());

        // Find data
        ArrayList<ConsumptionDataVo> data = consumedProductRepository.findAllByUserAndCreatedAtWithJPQL(user, monday);
        Map<String, Double> result = new LinkedHashMap<>();
        double sum = 0;
        for (ConsumptionDataVo vo : data) {
            double value = vo.getValue() == null ? 0 : vo.getValue().doubleValue();
            sum += value;
            result.put(vo.getType().getTitle(), value);
        }

        if (sum == 0)
            return new TrackerResDto(result);

        // Calculate portion
        for (String type : TYPES)
            result.replace(type, Math.round((result.get(type) / sum) * 10000) / 100.0);

        return new TrackerResDto(result);
    }
}
