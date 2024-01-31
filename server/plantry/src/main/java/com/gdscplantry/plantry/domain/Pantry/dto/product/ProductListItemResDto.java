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
public class ProductListItemResDto {
    private String icon;
    private String name;
    private Boolean isUseByDate;
    private Long days;
    private BigDecimal count;
    private Boolean isNotified;

    public ProductListItemResDto(String icon, String name, Boolean isUseByDate, LocalDate date, BigDecimal count, Boolean isNotified) {
        this.icon = icon == null ? "🍽️" : icon;
        this.name = name;
        this.isUseByDate = isUseByDate;
        this.days = LocalDate.now().until(date, ChronoUnit.DAYS);
        this.count = count;
        this.isNotified = isNotified;
    }

    public ProductListItemResDto(Product product, Boolean isNotified) {
        this.icon = product.getIcon() == null ? "🍽️" : product.getIcon();
        this.name = product.getName();
        this.isUseByDate = product.getIsUseByDate();
        this.days = LocalDate.now().until(product.getDate(), ChronoUnit.DAYS);
        this.count = product.getCount();
        this.isNotified = isNotified;
    }
}
