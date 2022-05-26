package com.example.visitservice.service;

import com.example.visitservice.model.Visit;
import com.example.visitservice.repository.TransactionLineRepository;
import com.example.visitservice.response.TransactionRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentServiceTest {
    @InjectMocks
    private PaymentService testService;
    
    @Mock
    private TransactionLineRepository mockTransactionLineRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPalCreditFromDuration_correctValue() {
        final double actual = PaymentService.getPalCreditFromDuration(100);

        assertEquals(85.0, actual);
    }

    @Test
    void getPalCreditFromDuration_roundsUpAt5() {
        final double actual = PaymentService.getPalCreditFromDuration(33.005 / 0.85);

        assertEquals(33.01, actual);
    }

    @Test
    void getPalCreditFromDuration_roundsDownUnder5() {
        final double actual = PaymentService.getPalCreditFromDuration(33.004 / 0.85);

        assertEquals(33.00, actual);
    }

    @Test
    void handlePaymentsForVisit_correctResponse() {
        final Visit visit = new Visit(1, 2, 3, 19138, 100.0, "nothing :)", "aaaa-bbbb-cccc-dddd");

        Mockito.when(mockTransactionLineRepository.saveAll(ArgumentMatchers.anyList())).thenReturn(null);

        final TransactionRecord actual = testService.handlePaymentsForVisit(visit);

        assertNotNull(actual);
        assertEquals(100.0, actual.getMemberFee());
        assertEquals(85.0, actual.getPalCredit());
        assertEquals(15.0, actual.getServiceFee());
        assertEquals("aaaa-bbbb-cccc-dddd", actual.getRecordLocator());
    }
}