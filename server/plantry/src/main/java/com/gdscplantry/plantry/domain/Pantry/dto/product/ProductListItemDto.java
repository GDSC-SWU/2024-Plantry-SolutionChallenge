package com.gdscplantry.plantry.domain.Pantry.dto.product;

import com.gdscplantry.plantry.domain.Pantry.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Builder
@AllArgsConstructor
@Getter
public class ProductListItemDto {
    private Long productId;
    private String icon;
    private String name;
    private Boolean isUseByDate;
    private Long days;
    private BigDecimal count;
    private String storage;
    private Boolean isNotified;

    public ProductListItemDto(Product product, Boolean isNotified) {
        this.productId = product.getId();
        this.icon = product.getIcon() == null ? "🍽️" : product.getIcon();
        this.name = product.getName();
        this.isUseByDate = product.getIsUseByDate();
        this.days = LocalDate.now().until(product.getDate(), ChronoUnit.DAYS);
        this.count = product.getCount();
        this.storage = product.getStorage().getKey();
        this.isNotified = isNotified;
    }
}
