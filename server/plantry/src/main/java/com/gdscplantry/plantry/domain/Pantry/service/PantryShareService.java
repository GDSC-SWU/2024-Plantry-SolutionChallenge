package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Pantry.domain.PantryRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.dto.share.CodeResDto;
import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.domain.Pantry.vo.PantryWithCodeVo;
import com.gdscplantry.plantry.domain.Pantry.vo.UserPantryWithCodeVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.error.exception.AppException;
import com.gdscplantry.plantry.global.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PantryShareService {
    private final UserPantryRepository userPantryRepository;
    private final PantryRepository pantryRepository;

    @Transactional(readOnly = true)
    public CodeResDto getCode(User user, Long pantryId) {
        // Find pantry data
        UserPantryWithCodeVo vo = userPantryRepository.findByUserAndPantryIdWithJPQL(user, pantryId)
                .orElseThrow(() -> new AppException(PantryErrorCode.PANTRY_NOT_FOUND));

        return new CodeResDto(vo.getPantryId(), vo.getCode());
    }

    @Transactional
    public CodeResDto refreshCode(User user, Long pantryId) {
        // Find pantry data
        PantryWithCodeVo vo = userPantryRepository.findPantryByUserAndPantryIdWithJPQL(user, pantryId)
                .orElseThrow(() -> new AppException(PantryErrorCode.PANTRY_NOT_FOUND));

        // Check owner
        if (!vo.getIsOwner())
            throw new AppException(PantryErrorCode.PANTRY_CODE_ACCESS_DENIED);

        // Refresh share code (must be unique)
        String code;
        do {
            code = RandomUtil.getRandomNickname();
        } while (!pantryRepository.existsAllByCode(code));

        // Update data
        vo.getPantry().updateCode(code);

        return new CodeResDto(pantryId, code);
    }
}
