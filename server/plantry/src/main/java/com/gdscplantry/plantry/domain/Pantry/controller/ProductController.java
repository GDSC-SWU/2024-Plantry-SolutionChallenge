package com.gdscplantry.plantry.domain.Pantry.controller;

import com.gdscplantry.plantry.domain.Pantry.dto.product.*;
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
        ProductItemResDto productItemResDto = productService.addSingleProduct(user, newProductReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(productItemResDto, 201));
    }

    @PostMapping
    public ResponseEntity<ResponseDto> addNewProducts(@Valid @RequestBody NewProductListReqDto newProductListReqDto, @RequestAttribute User user) {
        NewProductListResDto newProductListResDto = productService.addProducts(user, newProductListReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(newProductListResDto, 201));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto> updateProduct(@RequestParam("product") Long productId, @Valid @RequestBody UpdateProductReqDto updateProductReqDto, @RequestAttribute User user) {
        ProductItemResDto productItemResDto = productService.updateProduct(user, productId, updateProductReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(productItemResDto, 201));
    }

    @PatchMapping("/count")
    public ResponseEntity<ResponseDto> updateProductCount(@RequestParam("product") Long productId, @RequestParam("count") Double count, @RequestAttribute User user) {
        ProductItemResDto productItemResDto = productService.updateProductCount(user, productId, count);

        return ResponseEntity.status(201).body(DataResponseDto.of(productItemResDto, 201));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteProduct(@RequestParam("product") Long productId, @RequestParam("type") Integer type,
                                                     @RequestParam("count") Double count, @RequestAttribute User user) {
        DeleteProductResDto deleteProductResDto = productService.deleteProduct(user, productId, type, count);

        return ResponseEntity.ok(DataResponseDto.of(deleteProductResDto, 200));
    }

    @GetMapping
    public ResponseEntity<ResponseDto> readProductList(@RequestParam("pantry") Long pantryId, @RequestParam("filter") String filter, @RequestAttribute User user) {
        ProductListResDto productListResDto = productService.readProductList(user, pantryId, filter);

        return ResponseEntity.ok(DataResponseDto.of(productListResDto, 200));
    }
}
