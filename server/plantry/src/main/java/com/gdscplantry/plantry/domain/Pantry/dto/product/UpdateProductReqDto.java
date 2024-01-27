package com.gdscplantry.plantry.domain.Pantry.dto.product;

import com.gdscplantry.plantry.domain.Pantry.domain.Product;
import com.gdscplantry.plantry.domain.model.StorageEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class UpdateProductReqDto {
    private String icon;

    @NotBlank(message = "Name value required.")
    @Size(max = 12, message = "Invalid name value. (Max length is 12)")
    private String name;

    @NotNull(message = "IsUseByDate value required.")
    private Boolean isUseByDate;

    @NotBlank(message = "Date value required.")
    @Pattern(regexp = "^\\d{2,4}-\\d{1,2}-\\d{1,2}$", message = "Invalid date format. (yyyy-MM-dd)")
    private String date;

    @NotBlank(message = "Storage value required.")
    @Pattern(regexp = "^(Cold|Freeze|Etc)$", message = "Invalid storage value. (Cold / Freeze / Etc)")
    private String storage;

    @NotBlank(message = "Count value required.")
    @Pattern(regexp = "^\\d*\\.?[05]?$", message = "Invalid count value. (only 1 / 0.5 units)")
    private String count;

    public Product toEntity() {
        return Product.builder()
                .icon(icon)
                .name(name)
                .isUseByDate(isUseByDate)
                .date(LocalDate.parse(date))
                .storage(StorageEnum.findByKey(storage))
                .count(BigDecimal.valueOf(Float.parseFloat(count))).build();
    }
}
