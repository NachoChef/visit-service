package com.example.visitservice.model;

import com.example.visitservice.request.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.stream.Stream;

import static javax.persistence.GenerationType.AUTO;

/**
 * User entity, models every user and carries their basic info
 *
 * @author justinjones
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name="users")
public class User {

    /**
     * UserType will hold the status of the user, being either a pal or member
     *
     */
    public enum UserRole {
        // I will go with the assumption that pals do not also receive visits
        // modifying that would be easy enough to encapsulate here with additional params (visitable, etc)
        MEMBER("MEMBER"),
        PAL("PAL");

        private final String userType;

        UserRole(String type) {
            this.userType = type;
        }

        public UserRole fromString(String s) {
            return Stream.of(UserRole.values())
                    .filter(c -> c.asString().equals(s))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        public String asString() {
            return userType;
        }
    }

    /**
     * So this is obviously making an assumption that we have a default allocation that we always set,
     * which is probably fine for these purposes (also assuming insurance provides some standard XX hours monthly)
     * but we could also easily figure this out based on provider/etc
     */
    public static final double DEFAULT_ALLOCATION = 40.0;

    @Id
    @GeneratedValue(strategy = AUTO)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "balance")
    private double balance;

    /**
     * Create a User entity from a given request with the {@link User#DEFAULT_ALLOCATION} balance
     *
     * @param userRequest the incoming request we want to convert
     * @param userRole the user role we want to specify
     * @return a user Entity conforming to the supplied request, role and default allocation
     */
    public static User fromUserRequestAndRole(UserRequest userRequest, User.UserRole userRole) {
        User.UserBuilder builder = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .userRole(userRole);

        if (userRole.equals(UserRole.MEMBER)) {
            builder.balance(DEFAULT_ALLOCATION);
        } else {
            builder.balance(0.0);
        }

        return builder.build();
    }
}
