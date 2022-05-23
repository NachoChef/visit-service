package com.example.visitservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FulfillmentRequest {
    @NotNull
    @NumberFormat
    private int requestId;

    @NotNull
    @NumberFormat
    private int palId;

    @NotNull
    @NumberFormat
    private int memberId;

    @NotNull
    @NumberFormat
    private double minutesFulfilled;

    @NotBlank
    private String tasks;
}
