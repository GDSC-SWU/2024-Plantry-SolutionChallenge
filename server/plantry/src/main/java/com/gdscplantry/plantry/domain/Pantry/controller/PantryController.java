package com.gdscplantry.plantry.domain.Pantry.controller;

import com.gdscplantry.plantry.domain.Pantry.dto.NewPantryReqDto;
import com.gdscplantry.plantry.domain.Pantry.dto.NewPantryResDto;
import com.gdscplantry.plantry.domain.Pantry.dto.PantryListResDto;
import com.gdscplantry.plantry.domain.Pantry.service.PantryService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/pantry")
public class PantryController {
    private final PantryService pantryService;

    @GetMapping
    public ResponseEntity<ResponseDto> readPantryList(@RequestAttribute("user") User user) {
        PantryListResDto pantryListResDto = pantryService.readPantryList(user);

        return ResponseEntity.status(200).body(DataResponseDto.of(pantryListResDto, 200));
    }

    @PostMapping
    public ResponseEntity<ResponseDto> addNewPantry(@RequestBody @Valid NewPantryReqDto newPantryReqDto, @RequestAttribute("user") User user) {
        NewPantryResDto newPantryResDto = pantryService.addPantry(user, newPantryReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(newPantryResDto, 201));
    }
}
