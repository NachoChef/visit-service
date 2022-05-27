package com.example.visitservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Visit entity, models visits between members and pals that are recorded as a {@link TransactionLine}
 *
 * @author justinjones
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "visits")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "member_id")
    private int memberId;

    @Column(name = "pal_id")
    private int palId;

    @Column(name = "date")
    private long date;

    @Column(name = "duration")
    private double duration;

    @Column(name = "tasks")
    private String tasks;

    @Column(name = "record_locator")
    private String recordLocator;

    @PrePersist
    private void prePersist() {
        date = LocalDate.now().toEpochDay();
    }
}
