package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Pantry.domain.*;
import com.gdscplantry.plantry.domain.Pantry.dto.product.*;
import com.gdscplantry.plantry.domain.Pantry.error.PantryErrorCode;
import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.model.ProductDeleteTypeEnum;
import com.gdscplantry.plantry.global.error.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final PantryService pantryService;
    private final UserPantryRepository userPantryRepository;
    private final ConsumedProductRepository consumedProductRepository;

    @Transactional(readOnly = true)
    public Product validateProductId(User user, Long productId) {
        // Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(PantryErrorCode.PRODUCT_NOT_FOUND));

        // Check access rights
        if (!userPantryRepository.existsByPantryIdAndUser(product.getPantryId(), user))
            throw new AppException(PantryErrorCode.PRODUCT_ACCESS_DENIED);

        return product;
    }

    @Transactional
    public ProductItemResDto addSingleProduct(User user, NewProductReqDto dto) {
        // Find pantry & Check access rights
        pantryService.validatePantryId(user, dto.getPantry());

        // Find from Food Database

        // Save data
        Product product = dto.toEntity();
        productRepository.save(product);

        // Save default Notifications

        return new ProductItemResDto(product, true);
    }


    @Transactional
    public NewProductListResDto addProducts(User user, NewProductListReqDto dto) {
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
        ArrayList<ProductItemResDto> result = new ArrayList<>();
        for (Product product : products)
            result.add(new ProductItemResDto(product, true));

        return new NewProductListResDto(result);
    }

    @Transactional
    public ProductItemResDto updateProduct(User user, Long productId, UpdateProductReqDto updateProductReqDto) {
        // Find product & check access rights
        Product product = validateProductId(user, productId);

        // Check Notifications

        // Update notifications if notifications exist

        // Update product data
        product.updateProduct(updateProductReqDto.toEntity());

        return new ProductItemResDto(product, true);
    }

    @Transactional
    public ProductItemResDto updateProductCount(User user, Long productId, Double count) {
        // Find product & check access rights
        Product product = validateProductId(user, productId);

        // Count validation (1 / 0.5)
        if (count % 0.5 != 0 || count <= 0)
            throw new AppException(PantryErrorCode.INVALID_COUNT);

        // Update product data
        product.updateCount(BigDecimal.valueOf(count));

        // Check Notifications

        return new ProductItemResDto(product, true);
    }

    @Transactional
    public DeleteProductResDto deleteProduct(User user, Long productId, Integer type, Double reqCount) {
        // Find product & check access rights
        Product product = validateProductId(user, productId);

        // Count validation
        BigDecimal count = BigDecimal.valueOf(reqCount);
        if (reqCount % 0.5 != 0 || reqCount <= 0)
            throw new AppException(PantryErrorCode.INVALID_COUNT);
        else if (count.compareTo(product.getCount()) > 0)
            throw new AppException(PantryErrorCode.INVALID_DELETE_COUNT);

        // Find type
        ProductDeleteTypeEnum typeEnum = ProductDeleteTypeEnum.findByKey(type);

        // Add consumption data
        consumedProductRepository.save(ConsumedProduct.builder()
                .user(user)
                .count(count)
                .type(typeEnum)
                .product(product.getName())
                .foodDataId(product.getFoodDataId())
                .addedAt(product.getCreatedAt())
                .build()
        );

        // Update product data (delete if result count is 0)
        BigDecimal result = product.getCount().subtract(count);
        if (result.equals(BigDecimal.ZERO))
            productRepository.delete(product);
        else
            product.updateCount(result);

        return new DeleteProductResDto(product.getId(), typeEnum.getTitle(), result);
    }
}
