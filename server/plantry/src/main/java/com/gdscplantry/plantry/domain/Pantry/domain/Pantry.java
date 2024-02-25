package com.gdscplantry.plantry.domain.Pantry.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Pantry")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Pantry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String code;

    @CreatedDate
    private LocalDateTime createdAt;

    public Pantry(String uuid, String code) {
        this.uuid = uuid;
        this.code = code;
    }

    public void updateCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pantry pantry)) return false;

        return Objects.equals(this.id, pantry.getId()) &&
                Objects.equals(this.uuid, pantry.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid);
    }
}
