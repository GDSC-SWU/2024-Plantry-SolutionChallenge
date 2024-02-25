package com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission;

import com.gdscplantry.plantry.domain.User.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "AchievedMission")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AchievedMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mission mission;

    @CreatedDate
    private LocalDateTime createdAt;

    public AchievedMission(User user, Mission mission) {
        this.user = user;
        this.mission = mission;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AchievedMission achievedMission)) return false;

        return Objects.equals(this.id, achievedMission.getId()) &&
                Objects.equals(this.user, achievedMission.getUser()) &&
                Objects.equals(this.mission, achievedMission.getMission());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, mission);
    }
}
