package com.example.visitservice.model;

import com.example.visitservice.request.UserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {
    @Test()
    void fromUserRequestAndRole_exceptionOnNullRequest() {
        Assertions.assertThrows(NullPointerException.class, () -> User.fromUserRequestAndRole(null, User.UserRole.MEMBER));
    }

    @Test
    void fromUserRequestAndRole_createsExpectedMember() {
        final User expected = User.builder()
                .firstName("Bill")
                .lastName("Testerson")
                .email("billyt@testing.com")
                .userRole(User.UserRole.MEMBER)
                .build();

        final UserRequest userRequest = UserRequest.builder()
                .firstName("Bill")
                .lastName("Testerson")
                .email("billyt@testing.com")
                .build();
        final User actual = User.fromUserRequestAndRole(userRequest, User.UserRole.MEMBER);

        assertNotNull(actual);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(User.UserRole.MEMBER, actual.getUserRole());
        assertEquals(User.DEFAULT_ALLOCATION, actual.getBalance());
        assertEquals(0, expected.getId());
    }

    @Test
    void fromUserRequestAndRole_createsExpectedPal() {
        final User expected = User.builder()
                .firstName("Bill")
                .lastName("Testerson")
                .email("billyt@testing.com")
                .userRole(User.UserRole.PAL)
                .build();

        final UserRequest userRequest = UserRequest.builder()
                .firstName("Bill")
                .lastName("Testerson")
                .email("billyt@testing.com")
                .build();
        final User actual = User.fromUserRequestAndRole(userRequest, User.UserRole.PAL);

        assertNotNull(actual);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(User.UserRole.PAL, actual.getUserRole());
        assertEquals(0.0, actual.getBalance());
        assertEquals(0, expected.getId());
    }
}