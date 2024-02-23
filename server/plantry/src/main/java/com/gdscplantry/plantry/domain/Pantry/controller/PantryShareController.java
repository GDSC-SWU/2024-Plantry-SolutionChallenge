package com.gdscplantry.plantry.domain.Pantry.controller;

import com.gdscplantry.plantry.domain.Pantry.dto.pantry.PantryResDto;
import com.gdscplantry.plantry.domain.Pantry.dto.share.CodeResDto;
import com.gdscplantry.plantry.domain.Pantry.dto.share.PantryMemberResDto;
import com.gdscplantry.plantry.domain.Pantry.service.PantryShareService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @PatchMapping("/code")
    public ResponseEntity<ResponseDto> refreshCode(@RequestParam("pantry") Long pantryId, @RequestAttribute User user) {
        CodeResDto codeResDto = pantryShareService.refreshCode(user, pantryId);

        return ResponseEntity.status(201).body(DataResponseDto.of(codeResDto, 201));
    }

    @PostMapping("/code")
    public ResponseEntity<ResponseDto> postCode(@RequestParam("code") String code, @RequestAttribute User user) {
        PantryResDto pantryResDto = pantryShareService.postCode(user, code);

        return ResponseEntity.status(201).body(DataResponseDto.of(pantryResDto, 201));
    }

    @GetMapping("/member")
    public ResponseEntity<ResponseDto> getPantryMember(@RequestParam("pantry") Long pantryId, @RequestAttribute User user) {
        PantryMemberResDto pantryMemberResDto = pantryShareService.getPantryMember(user, pantryId);

        return ResponseEntity.ok(DataResponseDto.of(pantryMemberResDto, 200));
    }

    @GetMapping("/member/query")
    public ResponseEntity<ResponseDto> searchPantryMember(@RequestParam("pantry") @NotNull Long pantryId, @RequestParam("query") @NotBlank String query, @RequestAttribute User user) {
        PantryMemberResDto pantryMemberResDto = pantryShareService.searchPantryMember(user, pantryId, query);

        return ResponseEntity.ok(DataResponseDto.of(pantryMemberResDto, 200));
    }
}
