package com.example.visitservice.controller;

import com.example.visitservice.model.TransactionLine;
import com.example.visitservice.request.FulfillmentRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    @PostMapping("/visit/new")
    public @ResponseBody TransactionLine addVisitForRequest(@Valid @RequestBody FulfillmentRequest fulfillmentRequest) {


    }
}
