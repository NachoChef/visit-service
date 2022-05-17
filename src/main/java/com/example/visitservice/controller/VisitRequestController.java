package com.example.visitservice.controller;

import com.example.visitservice.model.VisitRequest;
import com.example.visitservice.repository.VisitRequestRepository;
import com.example.visitservice.request.NewVisitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Visit request controller to request new visits or cancel existing requests
 */
@Controller
@RequestMapping("/visit-request")
public class VisitRequestController {
    private VisitRequestRepository visitRequestRepository;

    @Autowired
    public VisitRequestController(VisitRequestRepository visitRequestRepository) {
        this.visitRequestRepository = visitRequestRepository;
    }

    @PostMapping("/new")
    public @ResponseBody VisitRequest createNewVisitRequest(@Valid @RequestBody NewVisitRequest newVisitRequest) {
        return visitRequestRepository.save(VisitRequest.fromRequest(newVisitRequest));
    }

    /**
     * This could also be a post. Will cancel a visit request by provided ID
     * TODO return 404 if the id does not correspond to a valid visit request
     * @param id
     * @return
     */
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<String> cancelVisitRequest(@PathVariable String id) {
        // this is easier for now than custom error binding/etc
        // yes I copy/pasted this :)
        try {
            visitRequestRepository.cancelRequestById(Integer.parseInt(id));
            return ResponseEntity.ok().build();
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().build();
        }
    }
}
