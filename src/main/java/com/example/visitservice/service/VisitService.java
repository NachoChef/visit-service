package com.example.visitservice.service;

import com.example.visitservice.exception.InvalidRequestStateException;
import com.example.visitservice.model.Visit;
import com.example.visitservice.model.VisitRequest;
import com.example.visitservice.repository.VisitRepository;
import com.example.visitservice.repository.VisitRequestRepository;
import com.example.visitservice.request.FulfillmentRequest;
import com.example.visitservice.response.TransactionRecord;
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

    /**
     * Given a fulfillment request, check eligibility for fulfillment. If eligible, calculate the credit/debit line items and enter into db
     *
     * @param fulfillmentRequest incoming request to fulfill a visit request
     * @return a transaction record including cost and credit breakdown, and a UUID4 record locator
     */
    public TransactionRecord handleVisitFulfillment(FulfillmentRequest fulfillmentRequest) {
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

        // now let the payment service handle the rest and return the response
        return paymentService.handlePaymentsForVisit(thisVisit);
    }

    /**
     * This wraps the repository lookup for encapsulation purposes.
     * This will throw a runtime exception if the ID isn't present!
     *
     * @param id record ID to search for
     * @return id
     */
    public Visit getVisitById(int id) {
        return visitRepository.findById(id).orElseThrow(() -> new InvalidRequestStateException(id));
    }

    /**
     * This wraps the repository update for encapsulation purposes.
     * Updated the provided ID {@link VisitRequest#active} value to false.
     * This does not confirm existence of the record!
     *
     * @param id record ID to perform the update on
     */
    public void cancelRequestById(int id) {
        visitRequestRepository.cancelRequestById(id);
    }
}
