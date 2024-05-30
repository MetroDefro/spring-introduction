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
@Table(name = "file")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int size;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Lob()
    @Column(nullable = false, length = 5000000)
    private byte[] contents;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "file")
    private Schedule schedule;

    public File update(String name, String type, int size, byte[] contents) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.contents = contents;
        return this;
    }
}
