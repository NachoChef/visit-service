package com.example.visitservice.service;

import com.example.visitservice.exception.InvalidRequestStateException;
import com.example.visitservice.model.Visit;
import com.example.visitservice.model.VisitRequest;
import com.example.visitservice.repository.VisitRepository;
import com.example.visitservice.repository.VisitRequestRepository;
import com.example.visitservice.request.FulfillmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This class holds the business logic and controls when/if/how we fulfill requests
 */
@Service
public class VisitService {

    private PaymentService paymentService;

    private VisitRepository visitRepository;

    private VisitRequestRepository visitRequestRepository;

    @Autowired
    public VisitService(PaymentService paymentService, VisitRepository visitRepository, VisitRequestRepository visitRequestRepository) {
        this.paymentService = paymentService;
        this.visitRepository = visitRepository;
        this.visitRequestRepository = visitRequestRepository;
    }

    public String handleVisitFulfillment(FulfillmentRequest fulfillmentRequest) {
        final VisitRequest existingVisitRequest = visitRequestRepository.findById(fulfillmentRequest.getRequestId()).orElseThrow(() -> new InvalidRequestStateException(fulfillmentRequest.getRequestId()));

        // make sure we haven't already fulfilled the request and/or it's still active
        if (existingVisitRequest.isFulfilled() || !existingVisitRequest.isActive()) {
            // if we have then that's another exception, can't double fulfill!
            // I'll se the same request state exception for ease
            throw new InvalidRequestStateException(fulfillmentRequest.getRequestId());
        }

        // at this point we've theoretically done all the validation we need, so we can start to handle the Visit
        final Visit thisVisit = Visit.builder()
                .memberId(fulfillmentRequest.getMemberId())
                .palId(fulfillmentRequest.getPalId())
                .duration(fulfillmentRequest.getMinutesFulfilled())
                .tasks(fulfillmentRequest.getTasks())
                .recordLocator(UUID.randomUUID().toString())
                .build();

        // save the visit record
        visitRepository.save(thisVisit);

        // now let the payment service handle the rest
        paymentService.handlePaymentsForVisit(thisVisit);

        return thisVisit.getRecordLocator();
    }

    public Visit getVisitById(int id) {
        return visitRepository.findById(id).orElseThrow(() -> new InvalidRequestStateException(id));
    }

    public void cancelRequestById(int id) {
        visitRequestRepository.cancelRequestById(id);
    }
}
