package com.example.agricultural_federation.services;

import com.example.agricultural_federation.dto.CreateMembershipFeeDto;
import com.example.agricultural_federation.entities.MembershipFee;
import com.example.agricultural_federation.entities.enums.ActivityStatus;
import com.example.agricultural_federation.exceptions.NotFoundException;
import com.example.agricultural_federation.repositories.CooperativeRepository;
import com.example.agricultural_federation.repositories.MembershipFeeRepository;
import com.example.agricultural_federation.validators.MembershipFeeValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipFeeService {

    private final MembershipFeeRepository membershipFeeRepository;
    private final CooperativeRepository cooperativeRepository;

    public MembershipFeeService(MembershipFeeRepository membershipFeeRepository, CooperativeRepository cooperativeRepository) {
        this.membershipFeeRepository = membershipFeeRepository;
        this.cooperativeRepository = cooperativeRepository;
    }

    public List<MembershipFee> getFeesByCoopId(String cooperativeId) {
        if (cooperativeRepository.findById(cooperativeId) == null) {
            throw new NotFoundException("Cooperative not found: " + cooperativeId);
        }

        return membershipFeeRepository.findByCooperativeId(cooperativeId).stream().toList();
    }

    public List<MembershipFee> createFees(String cooperativeId, List<CreateMembershipFeeDto> dtos) {
        if (cooperativeRepository.findById(cooperativeId) == null) {
            throw new NotFoundException("Collectivity not found: " + cooperativeId);
        }
        dtos.forEach(MembershipFeeValidator::validate);

        return dtos.stream().map(dto -> {
            MembershipFee fee = new MembershipFee();
            fee.setCooperativeId(cooperativeId);
            fee.setLabel(dto.getLabel());
            fee.setFrequency(dto.getFrequency());
            fee.setAmount(dto.getAmount());
            fee.setEligibleFrom(dto.getEligibleFrom());
            fee.setStatus(ActivityStatus.ACTIVE);
            return membershipFeeRepository.save(fee);
        }).toList();
    }

}
