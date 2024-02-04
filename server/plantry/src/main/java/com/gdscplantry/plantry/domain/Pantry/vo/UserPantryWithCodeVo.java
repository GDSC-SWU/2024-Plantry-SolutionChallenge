package com.gdscplantry.plantry.domain.Pantry.vo;

import com.gdscplantry.plantry.domain.User.domain.User;
import lombok.Getter;

@Getter
public class UserPantryWithCodeVo {
    private final User user;
    private final Long pantryId;
    private final String code;

    public UserPantryWithCodeVo(User user, Long pantryId, String code) {
        this.user = user;
        this.pantryId = pantryId;
        this.code = code;
    }
}
