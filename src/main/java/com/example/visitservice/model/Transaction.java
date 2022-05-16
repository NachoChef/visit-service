package com.example.visitservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Transaction entity, models transactions between members and pals with a reference to a given {@link Visit}
 *
 * @author justinjones
 */
@AllArgsConstructor
@Getter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "member_id")
    private int memberId;

    @Column(name = "pal_id")
    private int palId;

    @Column(name = "visit_id")
    private int visitId;
}
