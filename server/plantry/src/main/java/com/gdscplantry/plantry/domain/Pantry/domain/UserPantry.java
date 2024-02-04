package com.gdscplantry.plantry.domain.Pantry.domain;

import com.gdscplantry.plantry.domain.Pantry.dto.pantry.UpdatePantryReqDto;
import com.gdscplantry.plantry.domain.User.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "UserPantry")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class UserPantry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Long pantryId;

    private String title;

    private String color;

    private Boolean isMarked;

    private Boolean isOwner;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public UserPantry(User user, Long pantryId, String title, String color, Boolean isOwner) {
        this.user = user;
        this.pantryId = pantryId;
        this.title = title;
        this.color = color;
        this.isMarked = false;
        this.isOwner = isOwner;
    }

    public void updatePantry(UpdatePantryReqDto dto) {
        this.title = dto.getTitle();
        this.color = dto.getColor();
    }

    public Boolean updateIsMarked() {
        this.isMarked = !this.isMarked;

        return this.isMarked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserPantry userPantry)) return false;

        return Objects.equals(this.id, userPantry.getId()) &&
                Objects.equals(this.user, userPantry.getUser()) &&
                Objects.equals(this.pantryId, userPantry.getPantryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, pantryId);
    }
}
