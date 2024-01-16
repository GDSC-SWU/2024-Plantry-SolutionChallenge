package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Pantry.domain.Pantry;
import com.gdscplantry.plantry.domain.Pantry.domain.PantryRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantry;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.dto.pantry.*;
import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.error.exception.AppException;
import com.gdscplantry.plantry.global.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class PantryService {
    private final PantryRepository pantryRepository;
    private final UserPantryRepository userPantryRepository;

    public UserPantry validatePantryId(User user, Long pantryId) {
        // Find pantry
        UserPantry userPantry = userPantryRepository.findByPantryId(pantryId)
                .orElseThrow(() -> new AppException(PantryErrorCode.PANTRY_NOT_FOUND));

        // Check access rights
        if (!userPantry.getUser().equals(user))
            throw new AppException(PantryErrorCode.PANTRY_ACCESS_DENIED);

        return userPantry;
    }

    public void deletePantryData(UserPantry userPantry) {
        Long pantryId = userPantry.getPantryId();

        // Remove product data from pantry

        // Remove user pantry data
        userPantryRepository.delete(userPantry);

        // Remove pantry data if the pantry doesn't have any user
        if (!userPantryRepository.existsByPantryId(pantryId))
            pantryRepository.deleteById(pantryId);
    }

    @Transactional(readOnly = true)
    public PantryListResDto readPantryList(User user) {
        // Find list from DB
        ArrayList<PantryListItemDto> result = userPantryRepository.findAllByUserWithJPQL(user);

        // Push null
        result.add(null);

        return new PantryListResDto(result);
    }

    @Transactional
    public PantryResDto addPantry(User user, NewPantryReqDto dto) {
        // Add pantry
        Pantry pantry = pantryRepository.save(new Pantry(RandomUtil.getUuid()));

        // Set default color if color is null
        String color = dto.getColor() == null ? "FFA5A0" : dto.getColor();

        // Add user pantry
        UserPantry userPantry = UserPantry.builder()
                .user(user)
                .pantryId(pantry.getId())
                .title(dto.getTitle())
                .color(color)
                .build();
        userPantryRepository.save(userPantry);

        return new PantryResDto(userPantry);
    }

    @Transactional
    public PantryResDto updatePantry(User user, Long pantryId, UpdatePantryReqDto dto) {
        // Find pantry & Check access rights
        UserPantry userPantry = validatePantryId(user, pantryId);

        // Update pantry
        userPantry.updatePantry(dto);

        return new PantryResDto(userPantry);
    }

    @Transactional
    public void deletePantry(User user, Long pantryId) {
        // Find pantry & Check access rights
        UserPantry userPantry = validatePantryId(user, pantryId);

        // Remove pantry & related data
        deletePantryData(userPantry);
    }

    @Transactional
    public SetPantryMarkedResDto setPantryMarked(User user, Long pantryId) {
        // Find pantry & Check access rights
        UserPantry userPantry = validatePantryId(user, pantryId);

        // Update pantry marked
        Boolean result = userPantry.updateIsMarked();

        return new SetPantryMarkedResDto(pantryId, result);
    }
}
