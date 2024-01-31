package com.gdscplantry.plantry.domain.Notification.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateProductNotificationReqDto {
    @Size(max = 4)
    private ArrayList<Integer> list;
}
