package com.gdscplantry.plantry.domain.MyPage.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Terms")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Terms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String title;

    @Lob
    String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Terms terms)) return false;

        return Objects.equals(this.id, terms.getId()) &&
                Objects.equals(this.title, terms.getTitle()) &&
                Objects.equals(this.content, terms.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content);
    }
}
