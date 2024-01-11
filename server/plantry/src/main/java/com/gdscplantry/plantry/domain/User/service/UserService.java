package com.gdscplantry.plantry.domain.User.service;

import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.User.dto.GoogleLoginResDto;
import com.gdscplantry.plantry.domain.User.error.UserErrorCode;
import com.gdscplantry.plantry.domain.model.JwtVo;
import com.gdscplantry.plantry.global.error.exception.AppException;
import com.gdscplantry.plantry.global.util.FcmUtil;
import com.gdscplantry.plantry.global.util.GoogleOAuthUtil;
import com.gdscplantry.plantry.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final GoogleOAuthUtil googleOAuthUtil;
    private final FcmUtil fcmUtil;
    private final JwtUtil jwtUtil;

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
        User user = userRepository.findByEmail(googleUser.getEmail())
                .orElse(userRepository.save(googleUser));

        // Validate device-token
        fcmUtil.validateToken(deviceToken);

        // Update info (profile path & device token)
        user.updateUser(googleUser.getProfileImagePath(), deviceToken);

        // Generate JWT
        JwtVo jwtVo = jwtUtil.generateTokens(user);

        return new GoogleLoginResDto(user.getId(), user.getNickname(), jwtVo.getAccessToken(), jwtVo.getRefreshToken());
    }
}
