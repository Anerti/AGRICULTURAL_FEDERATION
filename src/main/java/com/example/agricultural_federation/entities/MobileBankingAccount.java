package com.example.agricultural_federation.entities;

import com.example.agricultural_federation.entities.enums.MobileBankingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MobileBankingAccount implements FinancialAccount {
    private String id;
    private String holderName;
    private MobileBankingService mobileBankingService;
    private String mobileNumber;
    private Long amount;
}
