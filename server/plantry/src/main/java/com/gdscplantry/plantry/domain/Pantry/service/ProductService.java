package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Pantry.domain.Product;
import com.gdscplantry.plantry.domain.Pantry.domain.ProductRepository;
import com.gdscplantry.plantry.domain.Pantry.domain.UserPantryRepository;
import com.gdscplantry.plantry.domain.Pantry.dto.product.*;
import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final PantryService pantryService;
    private final UserPantryRepository userPantryRepository;

    @Transactional
    public ProductItemDto addSingleProduct(User user, NewProductReqDto dto) {
        // Find pantry & Check access rights
        pantryService.validatePantryId(user, dto.getPantry());

        // Find from Food Database

        // Save data
        Product product = dto.toEntity();
        productRepository.save(product);

        // Save default Notifications

        return new ProductItemDto(product, true);
    }


    @Transactional
    public NewProductListDto addProducts(User user, NewProductListReqDto dto) {
        ArrayList<Product> products = new ArrayList<>();

        for (NewProductReqDto req : dto.getList()) {
            // Find pantry & Check access rights
            pantryService.validatePantryId(user, req.getPantry());

            // Find from Food Database

            // Add data
            products.add(req.toEntity());

            // Add default Notifications
        }

        // Save product data
        productRepository.saveAll(products);

        // Save notification data

        // Return dto
        ArrayList<ProductItemDto> result = new ArrayList<>();
        for (Product product : products)
            result.add(new ProductItemDto(product, true));

        return new NewProductListDto(result);
    }

    @Transactional
    public ProductItemDto updateProduct(User user, Long productId, UpdateProductReqDto updateProductReqDto) {
        // Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(PantryErrorCode.PRODUCT_NOT_FOUND));

        // Check access rights
        if (!userPantryRepository.existsByPantryIdAndUser(product.getPantryId(), user))
            throw new AppException(PantryErrorCode.PRODUCT_ACCESS_DENIED);

        // Check Notification

        // Update notification if notification exists

        // Update product data
        product.updateProduct(updateProductReqDto.toEntity());

        return new ProductItemDto(product, true);
    }
}
