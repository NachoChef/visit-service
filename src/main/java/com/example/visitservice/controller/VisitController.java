package com.example.visitservice.controller;

import com.example.visitservice.error.InvalidVisitRequestError;
import com.example.visitservice.exception.InvalidRequestStateException;
import com.example.visitservice.model.Visit;
import com.example.visitservice.request.FulfillmentRequest;
import com.example.visitservice.response.TransactionRecord;
import com.example.visitservice.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@Controller
@RequestMapping("/visit")
public class VisitController {

    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/new")
    public @ResponseBody TransactionRecord addVisitForRequest(@Valid @RequestBody FulfillmentRequest fulfillmentRequest) {
        // we'll pass off to the service to handle the rest
        return visitService.handleVisitFulfillment(fulfillmentRequest);
    }

    @GetMapping("/{id}")
    public @ResponseBody Visit getVisitById(@PathVariable String id) {
        return visitService.getVisitById(Integer.parseInt(id));
    }

    @ExceptionHandler(InvalidRequestStateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody InvalidVisitRequestError handleException(InvalidRequestStateException e) {
        return new InvalidVisitRequestError(e.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody InvalidVisitRequestError handleException(NumberFormatException e) {
        return new InvalidVisitRequestError("Visit request id was not in a valid format!");
    }
}
