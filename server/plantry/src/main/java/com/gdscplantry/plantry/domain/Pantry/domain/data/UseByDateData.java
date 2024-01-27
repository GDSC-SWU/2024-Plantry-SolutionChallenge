package com.gdscplantry.plantry.domain.Pantry.domain.data;

import com.gdscplantry.plantry.domain.model.StorageEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "UseByDateData")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class UseByDateData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private FoodData foodData;

    @Enumerated(EnumType.STRING)
    private StorageEnum storage;

    @NotNull
    private Integer value;

    @NotNull
    private Boolean isDay;

    private String description;

    @CreatedDate
    private LocalDateTime createdAt;
}
