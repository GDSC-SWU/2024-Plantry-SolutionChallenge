package com.gdscplantry.plantry.domain.Pantry.controller;

import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductItemDto;
import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductListDto;
import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductListReqDto;
import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductReqDto;
import com.gdscplantry.plantry.domain.Pantry.service.ProductService;
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
@RequestMapping("/api/v1/pantry/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/single")
    public ResponseEntity<ResponseDto> addNewProduct(@Valid @RequestBody NewProductReqDto newProductReqDto, @RequestAttribute User user) {
        NewProductItemDto newProductItemDto = productService.addSingleProduct(user, newProductReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(newProductItemDto, 201));
    }

    @PostMapping
    public ResponseEntity<ResponseDto> addNewProducts(@Valid @RequestBody NewProductListReqDto newProductListReqDto, @RequestAttribute User user) {
        NewProductListDto newProductListDto = productService.addProducts(user, newProductListReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(newProductListDto, 201));
    }
}
