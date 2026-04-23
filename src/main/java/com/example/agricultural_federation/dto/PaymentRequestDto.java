package com.example.agricultural_federation.dto;

import com.example.agricultural_federation.entities.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentRequestDto {
    private Long amount;
    private String membershipFeeIdentifier;
    private String accountCreditedIdentifier;
    private PaymentMode paymentMode;
}
