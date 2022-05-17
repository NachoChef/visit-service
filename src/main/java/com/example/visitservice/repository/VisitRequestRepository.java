package com.example.visitservice.repository;

import com.example.visitservice.model.VisitRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VisitRequestRepository extends CrudRepository<VisitRequest, Integer> {
    @Modifying
    @Query("update visit_requests v set v.active = 0 where v.id = :id")
    void cancelRequestById(@Param(value = "id") long id);
}
