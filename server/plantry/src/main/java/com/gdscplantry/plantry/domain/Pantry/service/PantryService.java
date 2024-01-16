package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Pantry.domain.Pantry;
import com.gdscplantry.plantry.domain.Pantry.domain.PantryRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantry;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.dto.NewPantryReqDto;
import com.gdscplantry.plantry.domain.Pantry.dto.NewPantryResDto;
import com.gdscplantry.plantry.domain.Pantry.dto.PantryListItemDto;
import com.gdscplantry.plantry.domain.Pantry.dto.PantryListResDto;
import com.gdscplantry.plantry.domain.User.domain.User;
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

    @Transactional(readOnly = true)
    public PantryListResDto readPantryList(User user) {
        // Find list from DB
        ArrayList<PantryListItemDto> result = userPantryRepository.findAllByUserWithJPQL(user);

        // Push null
        result.add(null);

        return new PantryListResDto(result);
    }

    @Transactional
    public NewPantryResDto addPantry(User user, NewPantryReqDto dto) {
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

        return new NewPantryResDto(userPantry);
    }
}
