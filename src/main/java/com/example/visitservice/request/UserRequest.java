package com.example.visitservice.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Request body for user creation, enforcing specific details are provided
 * User role (member or pal) will be decided based on the API endpoint
 */
@AllArgsConstructor
@Builder
@Getter
public class UserRequest {
    @NotBlank(message = "First name must not be blank!")
    private String firstName;

    @NotBlank(message = "Last name must not be blank!")
    private String lastName;

    @NotBlank(message = "Email must be present!")
    @Email(message = "Email must be a valid email!")
    private String email;
}
