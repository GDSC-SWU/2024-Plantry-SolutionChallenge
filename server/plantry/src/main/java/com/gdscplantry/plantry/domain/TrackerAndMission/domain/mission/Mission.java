package com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Mission")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private MissionData missionData;

    @CreatedDate
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mission mission)) return false;

        return Objects.equals(this.id, mission.getId()) &&
                Objects.equals(this.missionData, mission.getMissionData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, missionData);
    }
}
