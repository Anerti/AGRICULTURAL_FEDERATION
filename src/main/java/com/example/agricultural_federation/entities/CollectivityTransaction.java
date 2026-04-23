package com.example.agricultural_federation.entities;

import com.example.agricultural_federation.entities.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CollectivityTransaction {
    private String id;
    private LocalDateTime creationDate;
    private Long amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private Member memberDebited;
}
