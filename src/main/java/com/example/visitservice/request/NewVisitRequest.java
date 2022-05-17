package com.example.visitservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewVisitRequest {
    @NotNull(message = "Requestor id must be present!")
    @NumberFormat
    private int requestorId;
}
