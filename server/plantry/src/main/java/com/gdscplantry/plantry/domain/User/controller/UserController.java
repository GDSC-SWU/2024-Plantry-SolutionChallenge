package com.gdscplantry.plantry.domain.User.controller;

import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.dto.GoogleLoginResDto;
import com.gdscplantry.plantry.domain.User.service.UserService;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/google")
    public ResponseEntity<ResponseDto> googleLogin(@RequestHeader("id-token") String idToken, @RequestHeader("device-token") String deviceToken) {
        GoogleLoginResDto resDto = userService.googleLogin(idToken, deviceToken);

        return ResponseEntity.status(201).body(DataResponseDto.of(resDto, 201));
    }

    @DeleteMapping("/google")
    public ResponseEntity<ResponseDto> googleLogout(@RequestAttribute("user") User user) {
        userService.googleLogout(user);

        return ResponseEntity.ok(ResponseDto.of(200));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> removeUser(@RequestAttribute("user") User user) {
        userService.removeUser(user);

        return ResponseEntity.ok(ResponseDto.of(200));
    }

    @GetMapping("/token")
    public ResponseEntity<ResponseDto> refreshToken(@RequestHeader("Authorization-Refresh") String refreshToken, @RequestAttribute("user") User user) {
        GoogleLoginResDto resDto = userService.refreshToken(refreshToken, user);

        return ResponseEntity.status(201).body(DataResponseDto.of(resDto, 201));
    }
}
