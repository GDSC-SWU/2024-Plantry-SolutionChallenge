package com.gdscplantry.plantry.domain.User.controller;

import com.gdscplantry.plantry.domain.User.dto.GoogleLoginResDto;
import com.gdscplantry.plantry.domain.User.service.UserService;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/google")
    ResponseEntity<ResponseDto> googleLogin(@RequestHeader("id-token") String idToken, @RequestHeader("device-token") String deviceToken) {
        GoogleLoginResDto resDto = userService.googleLogin(idToken, deviceToken);

        return ResponseEntity.status(201).body(DataResponseDto.of(resDto, 201));
    }
}
