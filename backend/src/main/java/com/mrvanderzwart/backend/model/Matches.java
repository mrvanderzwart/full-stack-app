package com.mrvanderzwart.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="matches")
public class Matches {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "DATE")
    private String matchdate;

    @Column(name = "OPPONENT")
    private String opponent;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "COMPETITION")
    private String competition;
}