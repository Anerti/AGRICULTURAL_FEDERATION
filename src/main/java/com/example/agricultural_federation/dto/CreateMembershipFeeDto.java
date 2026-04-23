package com.example.agricultural_federation.dto;

import com.example.agricultural_federation.entities.enums.FrequencyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class CreateMembershipFeeDto {
    private String label;
    private FrequencyEnum frequency;
    private Long amount;
    private String eligibleFrom;
}

