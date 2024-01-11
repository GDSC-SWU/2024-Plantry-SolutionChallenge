package com.gdscplantry.plantry.domain.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class GoogleLoginResDto {
    private Long userId;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}
