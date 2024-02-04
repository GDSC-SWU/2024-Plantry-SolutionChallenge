package com.gdscplantry.plantry.domain.Pantry.controller;

import com.gdscplantry.plantry.domain.Pantry.dto.share.CodeResDto;
import com.gdscplantry.plantry.domain.Pantry.service.PantryShareService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/pantry/share")
public class PantryShareController {
    private final PantryShareService pantryShareService;

    @GetMapping("/code")
    public ResponseEntity<ResponseDto> getCode(@RequestParam("pantry") Long pantryId, @RequestAttribute User user) {
        CodeResDto codeResDto = pantryShareService.getCode(user, pantryId);

        return ResponseEntity.ok(DataResponseDto.of(codeResDto, 200));
    }
}
