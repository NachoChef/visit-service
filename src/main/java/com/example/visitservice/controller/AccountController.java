package com.example.visitservice.controller;

import com.example.visitservice.model.User;
import com.example.visitservice.repository.UserRepository;
import com.example.visitservice.request.UserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * AccountController
 *
 * This is a controller component that will handle all account-related CRUD requests
 *
 * @author justinjones
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    private final UserRepository userRepository;

    @Autowired
    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/member/create")
    public @ResponseBody User createMember(@Valid @RequestBody UserRequest userRequest) {
        return userRepository.save(User.fromUserRequestAndRole(userRequest, User.UserRole.MEMBER));
    }

    @PostMapping("/pal/create")
    public @ResponseBody User createPal(@Valid @RequestBody UserRequest userRequest) {
        return userRepository.save(User.fromUserRequestAndRole(userRequest, User.UserRole.PAL));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserInfo(@PathVariable String id) {
        // this is easier for now than custom error binding/etc
        try {
            return userRepository.findById(Integer.valueOf(id))
                    .map((u) -> {
                        try {
                            return ResponseEntity.ok(objectMapper.writeValueAsString(u));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().build();
        }
    }
}
