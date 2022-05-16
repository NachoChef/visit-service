package com.example.visitservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.AUTO;

/**
 * User entity, models every user and carries their basic info
 *
 * @author justinjones
 */
@AllArgsConstructor
@Getter
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
        MEMBER("member"),
        PAL("pal");

        private final String userType;

        UserRole(String type) {
            this.userType = type;
        }

        public String asString() {
            return userType;
        }
    }

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
    private UserRole userRole;

    @Column(name = "monthly_allocation")
    private double monthlyAllocation;
}
