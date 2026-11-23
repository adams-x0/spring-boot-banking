package com.bankingApp.banking_app.repository.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
@NoArgsConstructor
public class BankUser {

    private long userId;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private OffsetDateTime createdAt;




    public BankUser(String username,
                    String passwordHash,
                    String firstName,
                    String lastName,
                    String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = "CUSTOMER";
    }

    public BankUser(long userId,
                    String username,
                    String passwordHash,
                    String firstName,
                    String lastName,
                    String email,
                    String role,
                    OffsetDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }


}