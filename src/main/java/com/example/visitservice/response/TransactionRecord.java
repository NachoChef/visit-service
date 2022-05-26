package com.example.visitservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class TransactionRecord {
    /**
     * Total cost to member
     */
    private double memberFee;

    /**
     * Total credit to pal, minus the service fee
     */
    private double palCredit;

    /**
     * The service fee to us
     */
    private double serviceFee;

    /**
     * Record locator to track the transactions
     */
    private String recordLocator;
}
