package com.example.agricultural_federation.services;

import com.example.agricultural_federation.entities.CollectivityTransaction;
import com.example.agricultural_federation.entities.Cooperative;
import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.entities.Payment;
import com.example.agricultural_federation.repositories.CollectivityTransactionRepository;
import com.example.agricultural_federation.repositories.CooperativeRepository;
import com.example.agricultural_federation.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CollectivityTransactionService {

    private final CollectivityTransactionRepository transactionRepository;
    private final CooperativeRepository cooperativeRepository;
    private final MemberRepository memberRepository;

    public CollectivityTransactionService(CollectivityTransactionRepository transactionRepository,
                                          CooperativeRepository cooperativeRepository,
                                          MemberRepository memberRepository) {
        this.transactionRepository = transactionRepository;
        this.cooperativeRepository = cooperativeRepository;
        this.memberRepository = memberRepository;
    }

    public List<CollectivityTransaction> getTransactionsByDateRange(String collectivityId, LocalDate from, LocalDate to) {
        Cooperative cooperative = cooperativeRepository.findById(collectivityId);
        if (cooperative == null) {
            throw new com.example.agricultural_federation.exceptions.NotFoundException("Collectivity not found");
        }

        LocalDateTime fromDateTime = from.atStartOfDay();
        LocalDateTime toDateTime = to.atTime(LocalTime.MAX);

        return transactionRepository.findByCooperativeIdAndDateRange(
                collectivityId,
                Timestamp.valueOf(fromDateTime),
                Timestamp.valueOf(toDateTime)
        );
    }

    public CollectivityTransaction createFromPayment(Payment payment, String cooperativeId) throws java.sql.SQLException {
        CollectivityTransaction transaction = new CollectivityTransaction();
        transaction.setAmount(payment.getAmount());
        transaction.setPaymentMode(payment.getPaymentMode());
        transaction.setCreationDate(payment.getCreatedAt() != null ? payment.getCreatedAt() : LocalDateTime.now());

        Member member = memberRepository.findById(payment.getMemberId()).orElse(null);
        transaction.setMemberDebited(member);

        return transactionRepository.save(transaction);
    }
}