package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Notification.domain.Notification;
import com.gdscplantry.plantry.domain.Notification.domain.NotificationRepository;
import com.gdscplantry.plantry.domain.Notification.service.RelatedNotificationService;
import com.gdscplantry.plantry.domain.Pantry.domain.*;
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
    private final ProductRepository productRepository;
    private final NotificationRepository notificationRepository;
    private final RelatedNotificationService relatedNotificationService;

    @Transactional(readOnly = true)
    public UserPantry validatePantryId(User user, Long pantryId) {
        // Find pantry
        return userPantryRepository.findByPantryIdAndUser(pantryId, user)
                .orElseThrow(() -> new AppException(PantryErrorCode.PANTRY_NOT_FOUND));
    }

    @Transactional
    public void deletePantryData(UserPantry userPantry) {
        Long pantryId = userPantry.getPantryId();

        // Remove pantry share notifications
        notificationRepository.deleteAllByEntityIdAndTypeKeyGreaterThanEqual(pantryId, 10);

        // Remove exp notifications
        ArrayList<Notification> notifications = notificationRepository.findAllByPantryIdJoinProductWithJPQL(userPantry.getPantryId());
        notificationRepository.deleteAll(notifications);

        // Remove product data from pantry
        productRepository.deleteAllByPantryId(pantryId);

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
        // Set share code unique
        String code;
        do {
            code = RandomUtil.getRandomNickname();
        } while (!pantryRepository.existsAllByCode(code));

        // Add pantry
        Pantry pantry = pantryRepository.save(new Pantry(RandomUtil.getUuid(), code));

        // Set default color if color is null
        String color = dto.getColor() == null ? "FFA5A0" : dto.getColor();

        // Add user pantry
        UserPantry userPantry = UserPantry.builder()
                .user(user)
                .pantryId(pantry.getId())
                .title(dto.getTitle())
                .color(color)
                .isOwner(true)
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

        // Update notifications
        relatedNotificationService.updatePantry(user, userPantry);

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
