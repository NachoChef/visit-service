package com.example.visitservice.exception;

public class InvalidRequestStateException extends RuntimeException {
    public InvalidRequestStateException(int invalidRequestId) {
        super(invalidRequestId + " is not a valid request; it either doesn't exist or has already been fulfilled.");
    }
}
