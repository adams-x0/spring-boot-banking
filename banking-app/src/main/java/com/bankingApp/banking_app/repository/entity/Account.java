package com.bankingApp.banking_app.repository.entity;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Getter
@Setter
public class Account {

    private long accountId;
    private BigDecimal balance;
    private String status;
    private OffsetDateTime appliedAt;

    // Pending applications have no reviewer or reiew timestamp.
    private Long reviewedBy;
    private OffsetDateTime reviewedAt;

    public Account() {
        this.balance = BigDecimal.ZERO;
        this.status = "PENDING";
    }

    public Account(long accountId,
                   BigDecimal balance,
                   String status,
                   OffsetDateTime appliedAt,
                   Long reviewedBy,
                   OffsetDateTime reviewedAt) {
        this.accountId = accountId;
        this.balance = balance;
        this.status = status;
        this.appliedAt = appliedAt;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
    }


}