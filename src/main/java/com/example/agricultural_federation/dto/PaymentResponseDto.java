package com.example.agricultural_federation.dto;

import com.example.agricultural_federation.entities.enums.PaymentMode;
import lombok.Data;

@Data
public class PaymentResponseDto {
    private String id;
    private Long amount;
    private PaymentMode paymentMode;
    private AccountCreditedDto accountCreditedDto;
    private String creationDate;
}
