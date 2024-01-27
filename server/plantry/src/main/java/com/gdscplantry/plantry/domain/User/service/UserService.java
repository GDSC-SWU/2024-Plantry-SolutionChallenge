package com.gdscplantry.plantry.domain.User.service;

import com.gdscplantry.plantry.domain.Pantry.domain.ConsumedProductRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.PantryRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantry;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.service.PantryService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.User.dto.GoogleLoginResDto;
import com.gdscplantry.plantry.domain.User.error.UserErrorCode;
import com.gdscplantry.plantry.domain.model.JwtVo;
import com.gdscplantry.plantry.global.error.exception.AppException;
import com.gdscplantry.plantry.global.util.FcmUtil;
import com.gdscplantry.plantry.global.util.GoogleOAuthUtil;
import com.gdscplantry.plantry.global.util.JwtUtil;
import com.gdscplantry.plantry.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final GoogleOAuthUtil googleOAuthUtil;
    private final FcmUtil fcmUtil;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final PantryService pantryService;
    private final PantryRepository pantryRepository;
    private final UserPantryRepository userPantryRepository;
    private final ConsumedProductRepository consumedProductRepository;

    @Transactional
    public GoogleLoginResDto googleLogin(String idToken, String deviceToken) {
        if (idToken.isBlank())
            throw new AppException(UserErrorCode.ID_TOKEN_REQUIRED);
        else if (deviceToken.isBlank())
            throw new AppException(UserErrorCode.DEVICE_TOKEN_REQUIRED);

        // Authenticate id-token
        User googleUser = null;
        try {
            googleUser = googleOAuthUtil.authenticate(idToken);
        } catch (GeneralSecurityException | IOException e) {
            throw new AppException(UserErrorCode.INVALID_ID_TOKEN);
        }

        // Find user from DB
        User finalGoogleUser = googleUser;
        User user = userRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> userRepository.save(finalGoogleUser));

        // Validate device-token
        fcmUtil.validateToken(deviceToken);

        // Update info (profile path & device token)
        user.updateUser(googleUser.getProfileImagePath(), deviceToken);

        // Generate JWT
        JwtVo jwtVo = jwtUtil.generateTokens(user);

        // Save refreshToken to redis
        redisUtil.opsForValueSet(user.getId() + "_refresh", jwtVo.getRefreshToken(), 24 * 7);

        return new GoogleLoginResDto(user.getId(), user.getNickname(), jwtVo.getAccessToken(), jwtVo.getRefreshToken());
    }

    @Transactional
    public void googleLogout(User user) {
        // Remove login info
        redisUtil.delete(user.getId() + "_refresh");
    }

    @Transactional
    public void removeUser(User user) {
        // Remove data
        // 1. Products & Pantries
        ArrayList<UserPantry> pantries = userPantryRepository.findAllByUser(user);
        for (UserPantry userPantry : pantries)
            pantryService.deletePantryData(userPantry);

        // 2. Notifications

        // 3. Consumed Products
        consumedProductRepository.deleteAllByUser(user);

        // 4. Login data
        redisUtil.delete(user.getId() + "_refresh");

        // 5. User data
        userRepository.delete(user);
    }

    @Transactional
    public GoogleLoginResDto refreshToken(String refreshToken) {
        User tokenUser = jwtUtil.validateToken(false, refreshToken);

        // Generate JWT
        JwtVo jwtVo = jwtUtil.generateTokens(tokenUser);

        // Save refreshToken to redis
        redisUtil.opsForValueSet(tokenUser.getId() + "_refresh", jwtVo.getRefreshToken(), 24 * 7);

        return new GoogleLoginResDto(tokenUser.getId(), tokenUser.getNickname(), jwtVo.getAccessToken(), jwtVo.getRefreshToken());
    }
}
