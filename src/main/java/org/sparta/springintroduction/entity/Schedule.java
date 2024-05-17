package org.sparta.springintroduction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.springintroduction.dto.ScheduleRequestDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@Table(name = "schedule")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @Column(name = "charge", nullable = false)
    private String charge;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public Schedule update(String title, String contents, String charge) {
        this.title = title;
        this.contents = contents;
        this.charge = charge;
        return this;
    }
}
