package com.gdscplantry.plantry.domain.User.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "User")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String profileImagePath;

    private String deviceToken;

    private Integer notificationTime;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public User(@NotNull String email, String nickname, String profileImagePath) {
        this.email = email;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.notificationTime = 9;
    }

    public void updateUser(String profileImagePath, String deviceToken) {
        this.profileImagePath = profileImagePath;
        this.deviceToken = deviceToken;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateNotificationTime(Integer notificationTime) {
        this.notificationTime = notificationTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User user)) return false;

        return Objects.equals(this.id, user.getId()) &&
                Objects.equals(this.email, user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, nickname);
    }
}
