package com.gdscplantry.plantry.domain.Pantry.dto.product;

import com.gdscplantry.plantry.domain.Pantry.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Builder
@AllArgsConstructor
@Getter
public class NewProductItemDto {
    private Long pantryId;
    private Long productId;
    private String icon;
    private String name;
    private Boolean isUseByDate;
    private String date;
    private String storage;
    private Float count;
    private Boolean isNotified;

    public NewProductItemDto(Product product, Boolean isNotified) {
        this.pantryId = product.getPantryId();
        this.productId = product.getId();
        this.icon = product.getIcon();
        this.name = product.getName();
        this.isUseByDate = product.getIsUseByDate();
        this.date = isUseByDate ? product.getUseByDate().format(DateTimeFormatter.ISO_DATE) : product.getSellByDate().format(DateTimeFormatter.ISO_DATE);
        this.storage = product.getStorage().getKey();
        this.count = product.getCount().floatValue();
        this.isNotified = isNotified;
    }
}
