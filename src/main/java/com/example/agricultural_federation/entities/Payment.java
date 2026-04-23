package com.example.agricultural_federation.entities;

import com.example.agricultural_federation.entities.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Payment {
    private String id;
    private String memberId;
    private Long amount;
    private LocalDate paymentDate;
    private PaymentMode paymentMode;
    private String membershipFeeIdentifier;
    private String accountCreditedIdentifier;
    private LocalDate creationDate; // payment's date
    private LocalDateTime createdAt; // timestamp payment's date recorded in the db
}

