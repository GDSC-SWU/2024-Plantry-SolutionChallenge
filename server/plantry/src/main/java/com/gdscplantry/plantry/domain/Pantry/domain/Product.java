package com.gdscplantry.plantry.domain.Pantry.domain;

import com.gdscplantry.plantry.domain.Pantry.vo.FoodDataVo;
import com.gdscplantry.plantry.domain.model.StorageEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Product")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long pantryId;

    private Long foodDataId;

    private String icon;

    private String name;

    private Boolean isUseByDate;

    private LocalDate date;

    private LocalDate useByDateData;

    @Enumerated(EnumType.STRING)
    private StorageEnum storage;

    @Column(precision = 5, scale = 1)
    private BigDecimal count;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Product(@NotNull Long pantryId, String icon, String name, Boolean isUseByDate, LocalDate date, StorageEnum storage, BigDecimal count) {
        this.pantryId = pantryId;
        this.icon = icon;
        this.name = name;
        this.storage = storage;
        this.count = count;
        this.isUseByDate = isUseByDate;
        this.date = date;
    }

    public void updateProduct(Product product) {
        this.icon = product.getIcon();
        this.name = product.getName();
        this.storage = product.getStorage();
        this.count = product.getCount();
        this.isUseByDate = product.getIsUseByDate();
        this.date = product.getDate();
    }

    public void updateFoodData(FoodDataVo vo) {
        this.foodDataId = vo.getFoodDataId();
        this.icon = this.icon == null && vo.getEmoji() != null ? vo.getEmoji() : this.icon;
        this.useByDateData = vo.getUseByDateData();
    }

    public void updateCount(BigDecimal count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product product)) return false;

        return Objects.equals(this.id, product.getId()) &&
                Objects.equals(this.pantryId, product.getPantryId()) &&
                Objects.equals(this.name, product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pantryId, name);
    }
}