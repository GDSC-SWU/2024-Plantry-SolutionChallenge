package com.gdscplantry.plantry.domain.MyPage.service;

import com.gdscplantry.plantry.domain.MyPage.dto.UserProfileResDto;
import com.gdscplantry.plantry.domain.User.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    public UserProfileResDto getUserProfile(User user) {
        return new UserProfileResDto(user.getEmail(), user.getNickname(), user.getProfileImagePath());
    }
}
