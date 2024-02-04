package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.dto.share.CodeResDto;
import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.domain.Pantry.vo.UserPantryWithCodeVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PantryShareService {
    private final UserPantryRepository userPantryRepository;

    @Transactional(readOnly = true)
    public CodeResDto getCode(User user, Long pantryId) {
        // Find pantry data
        UserPantryWithCodeVo vo = userPantryRepository.findByUserAndPantryIdWithJPQL(user, pantryId)
                .orElseThrow(() -> new AppException(PantryErrorCode.PANTRY_NOT_FOUND));

        return new CodeResDto(vo.getPantryId(), vo.getCode());
    }
}
