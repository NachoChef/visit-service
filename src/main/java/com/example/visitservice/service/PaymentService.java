package com.example.visitservice.service;

import com.example.visitservice.model.TransactionLine;
import com.example.visitservice.model.Visit;
import com.example.visitservice.repository.TransactionLineRepository;
import com.example.visitservice.response.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PaymentService {

    private static final int SERVICE_ID = -1;
    private TransactionLineRepository transactionLineRepository;

    @Autowired
    public PaymentService(TransactionLineRepository transactionLineRepository) {
        this.transactionLineRepository = transactionLineRepository;
    }

    public TransactionRecord handlePaymentsForVisit(Visit visit) {
        // so at this point the visit is logged and we have a record locator
        // we now need to debit from the member's account and credit less 15% to the pal
        // then we will credit 15% to ourselves (sort of double entry bookkeeping, I suppose)
        // will also note I'm still dealing in hours here, not cash technically

        final double palCredit = getPalCreditFromDuration(visit.getDuration());
        final double serviceCharge = visit.getDuration() - palCredit;

        final TransactionLine memberDebitLine = TransactionLine.builder()
                .userId(visit.getMemberId())
                .recordLocator(visit.getRecordLocator())
                .lineType(TransactionLine.TransactionType.DEBIT)
                .amount(visit.getDuration())
                .build();

        final TransactionLine palCreditLine = TransactionLine.builder()
                .userId(visit.getPalId())
                .recordLocator(visit.getRecordLocator())
                .lineType(TransactionLine.TransactionType.CREDIT)
                .amount(palCredit)
                .build();

        final TransactionLine serviceCreditLine = TransactionLine.builder()
                .userId(SERVICE_ID)
                .recordLocator(visit.getRecordLocator())
                .lineType(TransactionLine.TransactionType.CREDIT)
                .amount(serviceCharge)
                .build();

        transactionLineRepository.saveAll(List.of(memberDebitLine, palCreditLine, serviceCreditLine));

        return TransactionRecord.builder()
                .memberFee(visit.getDuration())
                .palCredit(palCredit)
                .serviceFee(serviceCharge)
                .recordLocator(visit.getRecordLocator())
                .build();
    }

    public static double getPalCreditFromDuration(double duration) {
        final BigDecimal bd = new BigDecimal(duration * 0.85).setScale(2, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }
}
