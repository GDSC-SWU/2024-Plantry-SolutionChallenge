package com.gdscplantry.plantry.domain.Pantry.service;

import com.gdscplantry.plantry.domain.Pantry.domain.Product;
import com.gdscplantry.plantry.domain.Pantry.domain.ProductRepository;
import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductItemDto;
import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductListDto;
import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductListReqDto;
import com.gdscplantry.plantry.domain.Pantry.dto.product.NewProductReqDto;
import com.gdscplantry.plantry.domain.User.domain.User;
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

    @Transactional
    public NewProductItemDto addSingleProduct(User user, NewProductReqDto dto) {
        // Find pantry & Check access rights
        pantryService.validatePantryId(user, dto.getPantry());

        // Find from Food Database

        // Save data
        Product product = dto.toEntity();
        productRepository.save(product);

        // Save default Notifications

        return new NewProductItemDto(product, true);
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
        ArrayList<NewProductItemDto> result = new ArrayList<>();
        for (Product product : products)
            result.add(new NewProductItemDto(product, true));

        return new NewProductListDto(result);
    }
}