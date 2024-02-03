package com.gdscplantry.plantry.domain.MyPage.controller;

import com.gdscplantry.plantry.domain.MyPage.dto.NotificationTimeResDto;
import com.gdscplantry.plantry.domain.MyPage.dto.UpdateNicknameResDto;
import com.gdscplantry.plantry.domain.MyPage.dto.UserProfileResDto;
import com.gdscplantry.plantry.domain.MyPage.service.MyPageService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/nickname")
    public ResponseEntity<ResponseDto> updateNickname(@RequestParam @Valid @NotBlank(message = "Nickname value required.")
                                                      @Size(min = 2, max = 12, message = "Invalid nickname value. (2-12)")
                                                      String nickname,
                                                      @RequestAttribute User user) {
        UpdateNicknameResDto updateNicknameResDto = myPageService.updateNickname(user, nickname);

        return ResponseEntity.status(201).body(DataResponseDto.of(updateNicknameResDto, 201));
    }

    @PatchMapping("/notif")
    public ResponseEntity<ResponseDto> updateNotificationTime(@RequestParam @Valid @NotBlank(message = "Time value required.")
                                                              @Pattern(regexp = "^[1-9]{1}$|^[1]{1}\\d{1}$|^[2]{1}[0-4]{1}$", message = "Invalid time value. (1-24)")
                                                              Integer time,
                                                              @RequestAttribute User user) {
        NotificationTimeResDto notificationTimeResDto = myPageService.updateNotificationTime(user, time);

        return ResponseEntity.status(201).body(DataResponseDto.of(notificationTimeResDto, 201));
    }
}
