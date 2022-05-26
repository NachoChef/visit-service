package com.example.visitservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
 * Transaction entity, models transactions between members and pals with a reference to a given {@link Visit}
 *
 * @author justinjones
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "transactions")
public class TransactionLine {
    public enum TransactionType {
        CREDIT ("credit"),
        DEBIT("debit");

        private String transactionTypeStr;
        TransactionType(String type) {
                transactionTypeStr = type;
        }

        public String asString() {
            return transactionTypeStr;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "record_locator")
    private String recordLocator;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "line_type")
    private TransactionType lineType;

    @Column(name = "transaction_date")
    private long transactionDate;

    @Column(name = "amount")
    private double amount;

    @PrePersist
    private void prePersist() {
        transactionDate = LocalDate.now().toEpochDay();
    }
}
