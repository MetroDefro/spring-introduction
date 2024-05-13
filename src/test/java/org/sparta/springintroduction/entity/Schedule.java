package org.sparta.springintroduction.entity;

import jakarta.persistence.*;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "contents", nullable = false, length = 255)
    private String contents;

    @Column(name = "charge", nullable = false, length = 10)
    private String charge;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "date", nullable = false, length = 8)
    private int date;
}
