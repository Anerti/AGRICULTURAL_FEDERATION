package com.example.agricultural_federation.services;

import com.example.agricultural_federation.dto.AccountCreditedDto;
import com.example.agricultural_federation.dto.PaymentRequestDto;
import com.example.agricultural_federation.dto.PaymentResponseDto;
import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.entities.Payment;
import com.example.agricultural_federation.exceptions.BadRequestException;
import com.example.agricultural_federation.exceptions.NotFoundException;
import com.example.agricultural_federation.repositories.MemberRepository;
import com.example.agricultural_federation.repositories.PaymentRepository;
import com.example.agricultural_federation.validators.PaymentValidator;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;

    public PaymentService(PaymentRepository paymentRepository, MemberRepository memberRepository) {
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
    }

    public PaymentResponseDto recordPayment(String memberId, PaymentRequestDto dto) throws SQLException {
        PaymentValidator.validate(dto);

        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        if (!"active".equalsIgnoreCase(member.get().getStatus())) {
            throw new BadRequestException("Member is not active");
        }

        String feeCoopId = paymentRepository.findCoopIdByFeeIdentifier(dto.getMembershipFeeIdentifier());
        if (feeCoopId == null) {
            throw new NotFoundException("Membership fee not found: " + dto.getMembershipFeeIdentifier());
        }
        if (!feeCoopId.equals(member.get().getCollectivityId())) {
            throw new BadRequestException("Fee does not belong to member's cooperative");
        }

        boolean accountValid = paymentRepository.accountExistsForCoop(dto.getAccountCreditedIdentifier(), member.get().getCollectivityId());
        if (!accountValid) {
            throw new NotFoundException("Account not found or does not belong to member's cooperative: " + dto.getAccountCreditedIdentifier());
        }

        Payment payment = new Payment();
        payment.setMemberId(memberId);
        payment.setMembershipFeeIdentifier(dto.getMembershipFeeIdentifier());
        payment.setAccountCreditedIdentifier(dto.getAccountCreditedIdentifier());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMode(dto.getPaymentMode());

        Payment saved = paymentRepository.save(payment);
        return toDto(saved, dto.getAccountCreditedIdentifier());
    }

    private PaymentResponseDto toDto(Payment p, String accountId) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(p.getId());
        dto.setAmount(p.getAmount());
        dto.setPaymentMode(p.getPaymentMode());
        dto.setCreationDate(p.getCreationDate().toString());

        AccountCreditedDto accountCreditedDto = new AccountCreditedDto();
        accountCreditedDto.setId(accountId);
        accountCreditedDto.setAmount(p.getAmount());

        return dto;
    }
}


