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
 * Request entity, allows members to request visits that will be logged in transactions
 */
@AllArgsConstructor
@Getter
@Entity
@Table(name = "requests")
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "requestor_id")
    private int requestorId;

    @Column(name = "fulfilled")
    private boolean fulfilled;

    @Column(name = "transaction_id")
    private int transactionId;

    @Column(name = "requested_date")
    private LocalDate requestedDate;
}
