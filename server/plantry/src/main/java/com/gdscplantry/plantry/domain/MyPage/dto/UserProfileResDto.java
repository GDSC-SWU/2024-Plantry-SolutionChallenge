package com.gdscplantry.plantry.domain.MyPage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserProfileResDto {
    private String email;
    private String nickname;
    private String profileImagePath;
}
