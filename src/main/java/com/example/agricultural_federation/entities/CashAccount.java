package com.example.agricultural_federation.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CashAccount implements FinancialAccount {
    private String id;
    private Long amount;
}