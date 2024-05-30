package org.sparta.springintroduction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "file_id")
    private File file;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "schedule")
    private List<Comment> comments = new ArrayList<>();

    public Schedule update(String title, String contents, String charge) {
        this.title = title;
        this.contents = contents;
        this.charge = charge;
        return this;
    }
}
