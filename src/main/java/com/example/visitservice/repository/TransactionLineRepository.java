package com.example.visitservice.repository;

import com.example.visitservice.model.TransactionLine;
import org.springframework.data.repository.CrudRepository;

public interface TransactionLineRepository extends CrudRepository<TransactionLine, Integer> {

}
