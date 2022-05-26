package com.example.visitservice.service;

import com.example.visitservice.exception.InvalidRequestStateException;
import com.example.visitservice.model.Visit;
import com.example.visitservice.model.VisitRequest;
import com.example.visitservice.repository.VisitRepository;
import com.example.visitservice.repository.VisitRequestRepository;
import com.example.visitservice.request.FulfillmentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisitServiceTest {

    @InjectMocks
    private VisitService testService;

    @Mock
    private VisitRepository mockVisitRepository;

    @Mock
    private VisitRequestRepository mockVisitRequestRepository;

    @Mock
    private PaymentService mockPaymentService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVisitById_QueriesId() {
        final Visit fakeVisit = Visit.builder()
                .id(5)
                .memberId(1)
                .palId(2)
                .duration(23.9)
                .build();
        Mockito.when(mockVisitRepository.findById(ArgumentMatchers.eq(5))).thenReturn(Optional.of(fakeVisit));

        final Visit actual = testService.getVisitById(5);

        assertNotNull(actual);
        assertEquals(actual.getId(), fakeVisit.getId());
        assertEquals(actual.getMemberId(), fakeVisit.getMemberId());
        assertEquals(actual.getPalId(), fakeVisit.getPalId());
        assertEquals(actual.getDuration(), fakeVisit.getDuration());
    }

    @Test
    void getVisitById_throwsExceptionWhenNull() {
        Mockito.when(mockVisitRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

        final InvalidRequestStateException e = assertThrows(InvalidRequestStateException.class, () -> testService.getVisitById(100), "a non-existing entity was expected to throw an exception, but didn't!");
        assertEquals("100 is not a valid request; it either doesn't exist or has already been fulfilled.", e.getMessage());
    }

    @Test
    void handleVisitFulfillment_throwsExceptionOnNullEntity() {
        final FulfillmentRequest req = fulfillmentRequest();
        Mockito.when(mockVisitRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

        final InvalidRequestStateException e = assertThrows(InvalidRequestStateException.class, () -> testService.handleVisitFulfillment(req), "a non-existing entity was expected to throw an exception, but didn't!");
        assertEquals("100 is not a valid request; it either doesn't exist or has already been fulfilled.", e.getMessage());
    }

    @Test
    void handleVisitFulfillment_throwsExceptionOnDoubleFulfillment() {
        final FulfillmentRequest fulfillmentRequest = fulfillmentRequest();
        final VisitRequest visitRequest = VisitRequest.builder()
                .tasksRequested("nothing :)")
                .requestorId(2)
                .id(100)
                .active(true)
                .fulfilled(true)
                .build();;
        Mockito.when(mockVisitRequestRepository.findById(ArgumentMatchers.eq(fulfillmentRequest.getRequestId()))).thenReturn(Optional.of(visitRequest));

        final InvalidRequestStateException e = assertThrows(InvalidRequestStateException.class, () -> testService.handleVisitFulfillment(fulfillmentRequest), "a non-existing entity was expected to throw an exception, but didn't!");
        assertEquals("100 is not a valid request; it either doesn't exist or has already been fulfilled.", e.getMessage());
    }

    @Test
    void handleVisitFulfillment_throwsExceptionOnInactive() {
        final FulfillmentRequest fulfillmentRequest = fulfillmentRequest();
        final VisitRequest visitRequest = VisitRequest.builder()
                .tasksRequested("nothing :)")
                .requestorId(2)
                .id(100)
                .active(false)
                .fulfilled(false)
                .build();;
        Mockito.when(mockVisitRequestRepository.findById(ArgumentMatchers.eq(fulfillmentRequest().getRequestId()))).thenReturn(Optional.of(visitRequest));

        final InvalidRequestStateException e = assertThrows(InvalidRequestStateException.class, () -> testService.handleVisitFulfillment(fulfillmentRequest), "a non-existing entity was expected to throw an exception, but didn't!");
        assertEquals("100 is not a valid request; it either doesn't exist or has already been fulfilled.", e.getMessage());
    }

    @Test
    void handleVisitFulfillment_returnsRecordLocatorWhenValidRequest() {
        final FulfillmentRequest fulfillmentRequest = fulfillmentRequest();
        final VisitRequest visitRequest = visitRequest();

        Mockito.when(mockVisitRequestRepository.findById(ArgumentMatchers.eq(visitRequest.getId()))).thenReturn(Optional.of(visitRequest));
        Mockito.when(mockVisitRepository.save(ArgumentMatchers.any())).thenReturn(null);
        Mockito.doNothing().when(mockPaymentService).handlePaymentsForVisit(ArgumentMatchers.any());

        final String recordLocator = testService.handleVisitFulfillment(fulfillmentRequest);

        assertNotNull(recordLocator);
    }

    @Test
    void cancelRequestById_callsJPAMethod() {
        Mockito.doNothing().when(mockVisitRequestRepository).cancelRequestById(ArgumentMatchers.anyInt());

        testService.cancelRequestById(1);

        Mockito.verify(mockVisitRequestRepository, Mockito.times(1)).cancelRequestById(ArgumentMatchers.anyInt());
    }

    private FulfillmentRequest fulfillmentRequest() {
        return new FulfillmentRequest(100, 2, 3, 4.0, "nothing :)");
    }

    private VisitRequest visitRequest() {
        return VisitRequest.builder()
                .tasksRequested("nothing :)")
                .requestorId(2)
                .id(100)
                .active(true)
                .fulfilled(false)
                .build();
    }
}