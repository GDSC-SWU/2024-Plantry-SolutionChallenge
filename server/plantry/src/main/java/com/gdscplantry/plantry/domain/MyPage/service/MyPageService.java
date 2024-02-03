package com.gdscplantry.plantry.domain.MyPage.service;

import com.gdscplantry.plantry.domain.MyPage.dto.UpdateNicknameResDto;
import com.gdscplantry.plantry.domain.MyPage.dto.UserProfileResDto;
import com.gdscplantry.plantry.domain.MyPage.error.MyPageErrorCode;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    private final UserRepository userRepository;

    public UserProfileResDto getUserProfile(User user) {
        return new UserProfileResDto(user.getEmail(), user.getNickname(), user.getProfileImagePath());
    }

    @Transactional
    public UpdateNicknameResDto updateNickname(User user, String nickname) {
        // Check if nickname exists
        if (userRepository.existsByNickname(nickname))
            throw new AppException(MyPageErrorCode.NICKNAME_ALREADY_EXISTS);

        // Update data
        user.updateNickname(nickname);

        return new UpdateNicknameResDto(nickname);
    }
}
