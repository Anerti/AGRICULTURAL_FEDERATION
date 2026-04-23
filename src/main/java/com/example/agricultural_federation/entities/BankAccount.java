package com.example.agricultural_federation.entities;

import com.example.agricultural_federation.entities.enums.BankEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankAccount implements FinancialAccount {
    private String id;
    private String holderName;
    private BankEnum bankName;
    private Integer bankCode;
    private Integer bankBranchCode;
    private Integer bankAccountNumber;
    private Integer bankAccountKey;
    private Long amount;
}