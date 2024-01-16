package com.gdscplantry.plantry.domain.Pantry.controller;

import com.gdscplantry.plantry.domain.Pantry.dto.*;
import com.gdscplantry.plantry.domain.Pantry.service.PantryService;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.common.DataResponseDto;
import com.gdscplantry.plantry.global.common.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
        PantryResDto pantryResDto = pantryService.addPantry(user, newPantryReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(pantryResDto, 201));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto> updatePantry(@RequestParam("id") @NotNull Long pantryId,
                                                    @RequestBody @Valid UpdatePantryReqDto updatePantryReqDto,
                                                    @RequestAttribute("user") User user) {
        PantryResDto pantryResDto = pantryService.updatePantry(user, pantryId, updatePantryReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(pantryResDto, 201));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> deletePantry(@RequestParam("id") @NotNull Long pantryId, @RequestAttribute("user") User user) {
        pantryService.deletePantry(user, pantryId);

        return ResponseEntity.ok(ResponseDto.of(200));
    }

    @PatchMapping("/mark")
    public ResponseEntity<ResponseDto> setPantryMarked(@RequestParam("id") @NotNull Long pantryId,
                                                       @RequestAttribute("user") User user) {
        SetPantryMarkedResDto setPantryMarkedResDto = pantryService.setPantryMarked(user, pantryId);

        return ResponseEntity.status(201).body(DataResponseDto.of(setPantryMarkedResDto, 201));
    }
}
