package com.bankingApp.banking_app.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@NoArgsConstructor //JPA needs a no-argument constructor to load entities.
@Entity
@Table(name = "bank_users")
public class BankUserEntity {

//    Matches PostgreSQL's generated identity primary key.
//    A new unsaved user has a null ID
    @Id // Marks this field as the primary key of the entity.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Don't generate the ID in Java. Let the database generate it when the row is inserted.
    @Column(name = "user_id") // Maps the Java field to the database column
    private Long userId;

    @Column (name = "username", nullable = false, unique = true, length = 50)
    private String username;

//    Store the password hash, never the plain-text password.
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, length = 254)
    private String email;

//    New users default to CUSTOMER
//    The service controls any privileged role assignment.
    @Column(name = "role", nullable = false, length = 10)
    private String role = "CUSTOMER";

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

//    Constructor used when registering a new customer.
    public BankUserEntity(
            String username,
            String passwordHash,
            String email,
            String lastName,
            String firstName
    ) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public BankUserEntity(
            String firstName,
            Long userId,
            OffsetDateTime createdAt,
            String email, String role,
            String lastName,
            String passwordHash,
            String username
    ) {
        this.firstName = firstName;
        this.userId = userId;
        this.createdAt = createdAt;
        this.email = email;
        this.role = role;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.username = username;
    }

//    Supply the timestamp before JPA inserts a new user.
    @PrePersist // Run this method automatically right before the entity is inserted into the database.
    private void initializeCreatedAt() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        }
    }
}