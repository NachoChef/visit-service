package com.example.visitservice.model;

import com.example.visitservice.request.NewVisitRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Request entity, allows members to request visits that will be logged in transactions
 */
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "visit_requests")
public class VisitRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "requestor_id")
    private int requestorId;

    @Column(name = "fulfilled", columnDefinition = "tinyint(1) default 0")
    private boolean fulfilled;

    @Column(name = "transaction_id")
    private int transactionId;

    @Column(name = "requested_date")
    private long requestedDate;

    @Column(name = "active", columnDefinition = "tinyint(1) default 1")
    private boolean active;

    @Column(name = "tasks_requested")
    private String tasksRequested;

    @PrePersist
    private void prePersist() {
        requestedDate = LocalDate.now().toEpochDay();
    }

    /**
     * Static helper method to convert an incoming request into a VisitRequest
     * @param newVisitRequest request we want to convert
     * @return a visitrequest with the supplied requestor id
     */
    public static VisitRequest fromRequest(NewVisitRequest newVisitRequest) {
        return VisitRequest.builder()
                .requestorId(newVisitRequest.getRequestorId())
                .tasksRequested(newVisitRequest.getTasksRequested())
                .build();
    }
}
