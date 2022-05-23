package com.example.visitservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@Data
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

    @Column(name = "user_id")
    private int userId;

    @Column(name = "line_type")
    private TransactionType lineType;

    @Column(name = "transaction_date")
    private long transactionDate;
}
