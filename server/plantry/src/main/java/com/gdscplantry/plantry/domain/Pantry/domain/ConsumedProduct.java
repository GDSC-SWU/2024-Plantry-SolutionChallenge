package com.gdscplantry.plantry.domain.Pantry.domain;

import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.model.ProductDeleteTypeEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ConsumedProduct")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class ConsumedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(precision = 5, scale = 1)
    private BigDecimal count;

    @Enumerated(EnumType.STRING)
    private ProductDeleteTypeEnum type;

    private String product;

    private Long foodDataId;

    private LocalDateTime addedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public ConsumedProduct(User user, BigDecimal count, ProductDeleteTypeEnum type, String product, Long foodDataId, LocalDateTime addedAt) {
        this.user = user;
        this.count = count;
        this.type = type;
        this.product = product;
        this.foodDataId = foodDataId;
        this.addedAt = addedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ConsumedProduct consumedProduct)) return false;

        return Objects.equals(this.id, consumedProduct.getId()) &&
                Objects.equals(this.user, consumedProduct.getUser()) &&
                Objects.equals(this.type, consumedProduct.getType()) &&
                Objects.equals(this.product, consumedProduct.getProduct()) &&
                Objects.equals(this.addedAt, consumedProduct.getAddedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, type, product, addedAt);
    }
}
