package com.gdscplantry.plantry.domain.TrackerAndMission.domain.mission;

import com.gdscplantry.plantry.domain.model.MissionTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "MissionData")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MissionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private MissionTypeEnum type;

    @CreatedDate
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MissionData missionData)) return false;

        return Objects.equals(this.id, missionData.getId()) &&
                Objects.equals(this.title, missionData.getTitle()) &&
                Objects.equals(this.type, missionData.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type);
    }
}
