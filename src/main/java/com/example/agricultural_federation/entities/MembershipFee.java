package com.example.agricultural_federation.entities;

import com.example.agricultural_federation.entities.enums.ActivityStatus;
import com.example.agricultural_federation.entities.enums.FrequencyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class MembershipFee {
    private String id;
    private String cooperativeId;
    private String label;
    private ActivityStatus status;
    private FrequencyEnum frequency;
    private Long amount;
    private String eligibleFrom;
}
