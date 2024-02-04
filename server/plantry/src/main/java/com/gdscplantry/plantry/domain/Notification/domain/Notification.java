package com.gdscplantry.plantry.domain.Notification.domain;

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

    private Boolean isOff;

    private Boolean isDeleted;

    private LocalDateTime notifiedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Notification(User user, Integer typeKey, String title, String body, Long entityId, Boolean isDeleted, LocalDateTime notifiedAt) {
        this.user = user;
        this.typeKey = typeKey;
        this.title = title;
        this.body = body;
        this.entityId = entityId;
        this.isChecked = false;
        this.isOff = false;
        this.isDeleted = isDeleted == null ? notifiedAt.isBefore(LocalDateTime.now()) : isDeleted;
        this.notifiedAt = notifiedAt;
    }

    public void updateNotification(Integer typeKey, String title, String body, LocalDateTime notifiedAt) {
        this.typeKey = typeKey;
        this.title = title;
        this.body = body;
        this.notifiedAt = notifiedAt;
    }

    public void updateBody(String body) {
        this.body = body;
    }

    public void updateIsChecked() {
        this.isChecked = true;
    }

    public void updateIsOff(boolean isOff) {
        this.isOff = isOff;
    }

    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void updateNotifiedAt(LocalDateTime notifiedAt) {
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
