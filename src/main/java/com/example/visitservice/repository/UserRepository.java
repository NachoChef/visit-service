package com.example.visitservice.repository;

import com.example.visitservice.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * A basic CRUD repository for the User entity
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
