package com.gdscplantry.plantry.domain.MyPage.controller;

import com.gdscplantry.plantry.domain.MyPage.dto.UserProfileResDto;
import com.gdscplantry.plantry.domain.MyPage.service.MyPageService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/user")
    public ResponseEntity<ResponseDto> getUserProfile(@RequestAttribute User user) {
        UserProfileResDto userProfileResDto = myPageService.getUserProfile(user);

        return ResponseEntity.ok(DataResponseDto.of(userProfileResDto, 200));
    }
}
