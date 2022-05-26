package com.example.visitservice.repository;

import com.example.visitservice.model.Visit;
import org.springframework.data.repository.CrudRepository;

public interface VisitRepository extends CrudRepository<Visit, Integer> {
}
