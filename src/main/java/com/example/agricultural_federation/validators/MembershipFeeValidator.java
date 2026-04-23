package com.example.agricultural_federation.validators;

import com.example.agricultural_federation.dto.CreateMembershipFeeDto;
import com.example.agricultural_federation.exceptions.BadRequestException;

import java.util.List;

public class MembershipFeeValidator {

    private static final List<String> VALID_FREQUENCIES = List.of("MONTHLY", "ANNUAL", "ONE_TIME");

    public static void validate(CreateMembershipFeeDto dto) {
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new BadRequestException("amount must be positive");
        }
        if (dto.getFrequency() == null ||
                !VALID_FREQUENCIES.contains(String.valueOf(dto.getFrequency()))) {
            throw new BadRequestException("frequency must be MONTHLY, ANNUAL or ONE_TIME");
        }
        if (dto.getLabel() == null || dto.getLabel().isBlank()) {
            throw new BadRequestException("label is required");
        }
    }
}
