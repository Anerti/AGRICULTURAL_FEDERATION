package com.example.agricultural_federation.validators;

import com.example.agricultural_federation.dto.PaymentRequestDto;
import com.example.agricultural_federation.exceptions.BadRequestException;
import jakarta.validation.ValidationException;

import java.util.List;

public class PaymentValidator {

    private static final List<String> VALID_MODES = List.of("CASH", "BANK_TRANSFER", "MOBILE_MONEY");

    public static void validate(PaymentRequestDto dto) {
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new BadRequestException("amount must be a positive integer");
        }
        if (dto.getMembershipFeeIdentifier() == null || dto.getMembershipFeeIdentifier().isBlank()) {
            throw new BadRequestException("membershipFeeIdentifier is required");
        }
        if (dto.getAccountCreditedIdentifier() == null || dto.getAccountCreditedIdentifier().isBlank()) {
            throw new BadRequestException("accountCreditedIdentifier is required");
        }
        if (dto.getPaymentMode() == null || !VALID_MODES.contains(String.valueOf(dto.getPaymentMode()))) {
            throw new BadRequestException("paymentMode must be CASH, BANK_TRANSFER or MOBILE_MONEY");
        }
    }
}
