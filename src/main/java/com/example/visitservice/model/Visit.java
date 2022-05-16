package com.example.visitservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Visit entity, models visits between members and pals that are recorded as a {@link Transaction}
 *
 * @author justinjones
 */
@AllArgsConstructor
@Getter
@Entity
@Table(name = "visits")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "member_id")
    private int memberId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "duration")
    private double duration;

    @Column(name = "tasks")
    private String tasks;
}
