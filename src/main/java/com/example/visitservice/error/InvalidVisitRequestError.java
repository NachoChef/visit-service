package com.example.visitservice.error;

import lombok.Getter;

/**
 * This error nicely packs up the {@link InvalidVisitRequestError} into something that is suitable to return to an api client
 */
@Getter
public class InvalidVisitRequestError {
    private String errorMessage;

    public InvalidVisitRequestError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
