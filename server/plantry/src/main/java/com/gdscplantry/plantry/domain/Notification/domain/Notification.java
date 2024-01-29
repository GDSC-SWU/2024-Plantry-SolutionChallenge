package com.gdscplantry.plantry.domain.Notification.domain;

import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.model.NotificationTypeEnum;
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
@Table(name = "Notification")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Integer typeKey;

    private String title;

    private String body;

    private Long entityId;

    private Boolean isChecked;

    private LocalDateTime notifiedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Notification(User user, NotificationTypeEnum type, String title, String body, Long entityId, LocalDateTime notifiedAt) {
        this.user = user;
        this.typeKey = type.getKey();
        this.title = title;
        this.body = body;
        this.entityId = entityId;
        this.isChecked = false;
        this.notifiedAt = notifiedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Notification notification)) return false;

        return Objects.equals(this.id, notification.getId()) &&
                Objects.equals(this.user, notification.getUser()) &&
                Objects.equals(this.typeKey, notification.getTypeKey()) &&
                Objects.equals(this.entityId, notification.getEntityId()) &&
                Objects.equals(this.notifiedAt, notification.getNotifiedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, typeKey, entityId, notifiedAt);
    }
}
