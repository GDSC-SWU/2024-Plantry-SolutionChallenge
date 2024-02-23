package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Notification.service.RelatedNotificationService;
import com.gdscplantry.plantry.domain.Pantry.domain.Pantry;
import com.gdscplantry.plantry.domain.Pantry.domain.PantryRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantry;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.dto.pantry.PantryResDto;
import com.gdscplantry.plantry.domain.Pantry.dto.share.CodeResDto;
import com.gdscplantry.plantry.domain.Pantry.dto.share.PantryMemberResDto;
import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.domain.Pantry.vo.PantryMemberVo;
import com.gdscplantry.plantry.domain.Pantry.vo.PantryWithCodeVo;
import com.gdscplantry.plantry.domain.Pantry.vo.UserPantryWithCodeVo;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.error.exception.AppException;
import com.gdscplantry.plantry.global.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PantryShareService {
    private final UserPantryRepository userPantryRepository;
    private final PantryRepository pantryRepository;
    private final RelatedNotificationService relatedNotificationService;

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
        } while (pantryRepository.existsAllByCode(code));

        // Update data
        vo.getPantry().updateCode(code);

        return new CodeResDto(pantryId, code);
    }

    @Transactional
    public PantryResDto postCode(User user, String code) {
        // Find pantry data
        Pantry pantry = pantryRepository.findByCode(code)
                .orElseThrow(() -> new AppException(PantryErrorCode.INVALID_SHARE_CODE));

        // Find owner's pantry
        UserPantry ownerUserPantry = userPantryRepository.findByPantryIdAndIsOwner(pantry.getId(), true)
                .orElseThrow(() -> new AppException(PantryErrorCode.OWNER_NOT_FOUND));

        // Find if user already has one
        Optional<UserPantry> userPantry = userPantryRepository.findByPantryIdAndUser(pantry.getId(), user);
        if (userPantry.isPresent())
            throw new AppException(PantryErrorCode.PANTRY_ALREADY_EXISTS);

        // Add user pantry
        UserPantry newUserPantry = userPantryRepository.save(UserPantry.builder()
                .user(user)
                .pantryId(pantry.getId())
                .title(ownerUserPantry.getTitle())
                .color(ownerUserPantry.getColor())
                .isOwner(false)
                .build());

        // Add notifications
        relatedNotificationService.sharePantry(ownerUserPantry.getUser(), user, pantry.getId());

        return new PantryResDto(newUserPantry);
    }

    @Transactional(readOnly = true)
    public PantryMemberResDto getPantryMember(User user, Long pantryId) {
        // Validate pantry id
        UserPantry userPantry = userPantryRepository.findByPantryIdAndUser(pantryId, user)
                .orElseThrow(() -> new AppException(PantryErrorCode.PANTRY_NOT_FOUND));

        // Find member data
        ArrayList<PantryMemberVo> list = userPantryRepository.findAllByPantryIdWithJPQL(user, pantryId);

        return new PantryMemberResDto(userPantry.getIsOwner(), list);
    }

    @Transactional(readOnly = true)
    public PantryMemberResDto searchPantryMember(User user, Long pantryId, String query) {
        // Validate pantry id
        UserPantry userPantry = userPantryRepository.findByPantryIdAndUser(pantryId, user)
                .orElseThrow(() -> new AppException(PantryErrorCode.PANTRY_NOT_FOUND));

        // Find member with query
        ArrayList<PantryMemberVo> list = userPantryRepository.findAllByPantryIdAndQueryWithJPQL(user, pantryId, query);

        return new PantryMemberResDto(userPantry.getIsOwner(), list);
    }
}
