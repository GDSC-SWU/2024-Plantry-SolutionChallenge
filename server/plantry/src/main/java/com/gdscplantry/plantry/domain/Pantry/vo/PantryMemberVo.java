package com.gdscplantry.plantry.domain.Pantry.vo;

import lombok.Getter;

@Getter
public class PantryMemberVo {
    private final Long userId;
    private final String nickname;
    private final String profileImagePath;
    private final Boolean isOwner;

    public PantryMemberVo(Long userId, String nickname, String profileImagePath, Boolean isOwner) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.isOwner = isOwner;
    }
}
