package com.gdscplantry.plantry.domain.MyPage.controller;

import com.gdscplantry.plantry.domain.MyPage.dto.*;
import com.gdscplantry.plantry.domain.MyPage.service.MyPageService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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

    @GetMapping("/notif")
    public ResponseEntity<ResponseDto> getNotificationTime(@RequestAttribute User user) {
        NotificationTimeResDto notificationTimeResDto = myPageService.getNotificationTime(user);

        return ResponseEntity.ok(DataResponseDto.of(notificationTimeResDto, 200));
    }

    @PatchMapping("/notif")
    public ResponseEntity<ResponseDto> updateNotificationTime(@RequestParam @Valid @NotNull(message = "Time value required.")
                                                              @Min(value = 0, message = "Invalid time. (1-24)") @Max(value = 24, message = "Invalid time. (1-24)")
                                                              Integer time,
                                                              @RequestAttribute User user) {
        NotificationTimeResDto notificationTimeResDto = myPageService.updateNotificationTime(user, time);

        return ResponseEntity.status(201).body(DataResponseDto.of(notificationTimeResDto, 201));
    }

    @GetMapping("/notif/permission")
    public ResponseEntity<ResponseDto> getNotificationPermission(@RequestAttribute User user) {
        NotificationPermissionResDto notificationPermissionResDto = myPageService.getNotificationPermission(user);

        return ResponseEntity.ok(DataResponseDto.of(notificationPermissionResDto, 200));
    }

    @PatchMapping("/notif/permission")
    public ResponseEntity<ResponseDto> updateNotificationPermission(@RequestAttribute User user) {
        NotificationPermissionResDto notificationPermissionResDto = myPageService.updateNotificationPermission(user);

        return ResponseEntity.status(201).body(DataResponseDto.of(notificationPermissionResDto, 201));
    }

    @GetMapping("/terms")
    public ResponseEntity<ResponseDto> getTerms(@RequestParam @Valid @NotBlank(message = "Type required.") String type) {
        TermsResDto termsResDto = myPageService.getTerms(type);

        return ResponseEntity.ok(DataResponseDto.of(termsResDto, 200));
    }
}
